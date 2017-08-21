import groovy.transform.Field

def filePath = 'ui/library.gradle'

@Field String sdkVersion
node(node_label) {
    echo 'pay script starts'
//    configEnv()
//    configFileProvider(
//            [configFile(
//                    fileId: '5773ddd9-d493-4dd5-bda0-81a42c261cd7',
//                    targetLocation: "${WORKSPACE}/"
//            )]
//    ) {


    configSdkVersion()
    println "sdk version = ${deploy()}"
    println "sdk version = ${deploy()}"
    println filePath

    try {
        timeout(time: 1, unit: 'MINUTES') {
            echo ' deploy success'
        }
    } catch (e) {
    }
//    }
    echo 'pay script done'
}

def deploy() {
    if (sdkVersion != null) {
        return sdkVersion
    }
    throw new Exception('empty sdk version')
}

def configSdkVersion() {
    def buildFilePath = '/Users/lint/Desktop/AndroidProject/CITest/build.gradle'
    String source = readFile buildFilePath
    List<String> stringList = source.readLines()
    for (String line: stringList) {
        println line
        if (line.contains('sdk_version')) {
            sdkVersion = line.substring(line.indexOf('=') + 1, line.length()).trim()
            sdkVersion = sdkVersion.substring(1, sdkVersion.length() - 1)
            mainVersion = sdkVersion
            println sdkVersion
            if(sdkVersion != null) {
                sdkVersion += (isDebug() ? '-SNAPSHOT' : '')
                return sdkVersion
            }
        }
    }
    if (sdkVersion != null && !sdkVersion.isEmpty()) {
        source = source.replace(sdkVersion)
        writeFile file: buildFilePath, text: source
        println 'config version'
        source = readFile buildFilePath
        println source
    }
}

def isDebug() {
    println "debug ${debug}"
    return debug == 'true'
}
