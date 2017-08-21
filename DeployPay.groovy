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


    pritnln "sdk version = ${deploy()}"
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
    String source = new File('/Users/lint/Desktop/eleme/pay/build.gradle').text
    println source
    String sdkVersion
    List<String> stringList = source.readLines()
    stringList.each {
        println it
        if (it.contains('sdk_version')) {
            sdkVersion = it.substring(it.indexOf('=') + 1, it.length()).trim()
        }
    }
    if (sdkVersion == null) throw new Exception('empty sdk version')
    sdkVersion
}
