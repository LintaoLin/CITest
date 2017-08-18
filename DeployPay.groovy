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


    deploy()
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
    source.eachLine {
        println(it)
    }
}
