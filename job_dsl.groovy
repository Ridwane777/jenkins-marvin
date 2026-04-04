import jenkins.model.*
import hudson.model.*
import com.cloudbees.hudson.plugins.folder.*

def instance = Jenkins.getInstance()

def folderName = "Tools"
def folderDescription = "Folder for miscellaneous tools."
def existingItem = instance.getItem(folderName)

if (existingItem == null) {
    Folder folder = instance.createProject(Folder.class, folderName)
    folder.setDescription(folderDescription)
    folder.save()
}
def toolsFolder = instance.getItem(folderName)

def jobName = "clone-repository"
def myJob = toolsFolder.getItem(jobName)

if (myJob == null) {
    freeStyleJob('Tools/clone-repository') {
        description 'Job to clone a repository via Git.'
        
        parameters {
            stringParam('GIT_REPOSITORY_URL', '', 'Git URL of the repository to clone')
        }
        
        wrappers {
            preBuildCleanup()
        }
        
        steps {
            shell('git clone $GIT_REPOSITORY_URL .')
        }
    }
}

def seedJobName = "SEED"
def seedJob = toolsFolder.getItem(seedJobName)
 
if (seedJob == null) {
    freeStyleJob('Tools/SEED') {
        description 'Job that generates CI jobs from a GitHub repository.'
        
        parameters {
            stringParam('GITHUB_NAME', '', 'GitHub repository owner/repo_name (e.g.: "your-org/your-c-project")')
            stringParam('DISPLAY_NAME', '', 'Display name for the job')
        }
        
        steps {
            dsl {
                text('''
                freeStyleJob("${DISPLAY_NAME}") {
                    displayName("${DISPLAY_NAME}")
                    
                    properties {
                        githubProjectUrl("https://github.com/${GITHUB_NAME}/")
                    }
                
                    scm {
                        git {
                            remote {
                                url("https://github.com/${GITHUB_NAME}.git")
                            }
                            branches('master', 'main')
                        }
                    }
                
                    triggers {
                        scm('* * * * *')
                    }
                
                    wrappers {
                        preBuildCleanup()
                    }
                
                    steps {
                        shell('make fclean')
                        shell('make')
                        shell('make tests_run')
                        shell('make clean')
                    }
                }
                ''')
                setRemovedJobAction('DELETE')
                setRemovedViewAction('DELETE')
                lookupStrategy('SEED_JOB')
            }
        }
    }
}

instance.save()