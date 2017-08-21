def filePath = 'ui/library.gradle'
node(node_label) {
    echo 'pay script starts'
//    configEnv()
//    configFileProvider(
//            [configFile(
//                    fileId: '5773ddd9-d493-4dd5-bda0-81a42c261cd7',
//                    targetLocation: "${WORKSPACE}/"
//            )]
//    ) {


    println "sdk version = ${deploy()}"
    println "sdk version = ${deploy()}"

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
    String source = new File('/Users/lint/Desktop/eleme/pay/build.gradle').text

    List<String> stringList = source.readLines()
    for (String line: stringList) {
        println line
        if (line.contains('sdk_version')) {
            sdkVersion = line.substring(line.indexOf('=') + 1, line.length()).trim()
            sdkVersion = sdkVersion.substring(1, sdkVersion.length() - 1)
            println sdkVersion
            if(sdkVersion != null) {
                println 'break loop'
                return sdkVersion
            }
        }
    }
    throw new Exception('empty sdk version')
}
