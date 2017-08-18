def filePath = 'ui/library.gradle'
node(node_label) {
    echo 'pay script starts'
//    configEnv()
    cloneRepo()
//    configFileProvider(
//            [configFile(
//                    fileId: '5773ddd9-d493-4dd5-bda0-81a42c261cd7',
//                    targetLocation: "${WORKSPACE}/"
//            )]
//    ) {
        deploy(filePath)
        try {
            timeout(time: 1, unit: 'MINUTES') {
                echo ' deploy success'
            }
        } catch (e) {
        }
//    }
    echo 'pay script done'
}

def cloneRepo() {
    stage 'clone pay module repo'
    git (
            branch: "develop",
            credentialsId: '1efbdf54-7156-4f91-8a59-2d062981aa15',
            url: "git@git.elenet.me:eleme.mobile.android/pay.git"
    )
}
def deployVersion() {
    sdk_version
}
def deployBranch() {
    'develop'
}
def deployVersionName() {
    def version = deployVersion()
    if (version) {
        return "*$version*"
    }
    return ''
}

def deploy(filePath) {
    stage 'deploy'
    configProxy()
    configSdk()
    deployLibrary(filePath)
    deployUI(filePath)
}

def deployLibrary(String filePath) {
    echo 'deploy library'
//    new File(filePath).write('dependencies { \n compile project(\':library\')\n}')
    writeFile file: filePath, text: 'dependencies { \n compile project(\':library\')\n}'
    println 'deploy library'
    uploadArchives('library')
}

def deployUI(String filePath) {
    echo 'deply ui'
//    new File(filePath).write('dependencies { \n compile \"me.ele:pay-core:\$sdk_version\" \n}')
    writeFile file: filePath, text: 'dependencies { \n compile \"me.ele:pay-core:\$sdk_version\" \n}'
    uploadArchives('ui')
    println 'deploy ui'
}

def readLastBuildRevision(lastRevFile) {
    lastRevFile.parentFile.mkdirs()
    if (lastRevFile.exists() && lastRevFile.text) {
        def version = deployVersion()
        if (version) {
            println "module/${gitlabSourceRepoName}/${version} last build revision: ${lastRevFile.text}"
        } else {
            println "module/${gitlabSourceRepoName} last build revision: ${lastRevFile.text}"
        }
        return lastRevFile.text
    }
    return null
}

def writeLastBuildRevision(lastRevFile) {
    def rev = sh returnStdout: true, script: "git rev-parse HEAD"
    println "write build revision: $rev"
    if (rev) {
        rev = rev.trim()
        lastRevFile.write(rev)
    }
}

def isDebug() {
    println "debug ${debug}"
    return debug == 'true'
}

def uploadArchives(repo) {
    println "upload $repo"
//    def cmd = "./gradlew $repo:uploadArchives"
    def cmd = 'ls -a'
    try {
        def process = cmd.execute()
        process.waitFor()
        process.errorStream.eachLine {
            println(it)
        }
        process.inputStream.eachLine {
            println(it)
        }
    } catch (Exception err) {
        println err
    }
}

def configSdk() {
    def content = readFile 'local.properties'
    writeFile file: 'local.properties', text: content + "\nsdk.dir=$ANDROID_SDK"
}
def configProxy() {
    def content = readFile 'gradle.properties'
    writeFile file: 'gradle.properties', text: content + "\nsystemProp.http.proxyHost=127.0.0.1\nsystemProp.http.proxyPort=7777\nsystemProp.https.proxyHost=127.0.0.1\nsystemProp.https.proxyPort=7777"
}

def deploySucceedSummary() {
    def repoHttpUrl = "$gitlabSourceRepoHttpUrl".replace('.git', '')
    return isDebug() ?
            "deploy <$repoHttpUrl|$gitlabSourceRepoName> module ${deployVersionName()} `succeed`":
            "<!channel>\nrelease <$repoHttpUrl|$gitlabSourceRepoName> module ${deployVersionName()} `succeed`"
}
def deployDetail() {
    return " at branch ${deployBranch()} job <$env.BUILD_URL|$env.BUILD_DISPLAY_NAME>\n${changelog()}"
}
def deployFailedSummary() {
    "${mention()}\n${isDebug()? 'deploy':'release'} *${gitlabSourceRepoName}* module ${deployVersionName()} `failed` (<${env.BUILD_URL}/console|log>)"
}

def configEnv() {
    jdk = tool name: jdk_version
    env.JAVA_HOME = "${jdk}"
    echo "jdk installation path is: ${jdk}"
    sh '$JAVA_HOME/bin/java -version'
}

def notifySucceed() {
    slackSend (
            channel: slack_channel,
            color: '#36A64F',
            failOnError: false,
            message: deploySucceedSummary() + deployDetail(),
            teamDomain: 'eleme',
            token: slack_token
    )
}
def notifyFailed() {
    slackSend (
            channel: slack_channel,
            color: '#D31A5F',
            failOnError: false,
            message: deployFailedSummary() + deployDetail(),
            teamDomain: 'eleme',
            token: slack_token
    )
}