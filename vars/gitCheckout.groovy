def call(Map stageParams) {
 
    checkout([
        $class: 'GitSCM',
        branches: [[name:  stageParams.branch ]],
        userRemoteConfigs: [[ url: stageParams.url ]],
        extensions: [[$class: 'LocalBranch']]
    ])
  }

def check_branch() {
    println "You're on branch:"
    sh "git branch"
}