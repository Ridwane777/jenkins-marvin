folder('Tools') {
    description('Folder for miscellaneous tools.')
}

freeStyleJob('Tools/clone-repository') {
    description('Job to clone a repository via Git.')
    
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

freeStyleJob('Tools/SEED') {
    description('Job that generates CI jobs from a GitHub repository.')
    
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