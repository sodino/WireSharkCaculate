import groovy.json.JsonSlurper

def logFile = '20x2.json'
def baseDir = "/Users/sodino/TLS/"



def inputFile = new File(baseDir + logFile)
def fixJsonFile = new File(baseDir + '20x2.fix.json')
fixJsonFile.delete()



def fixLine = new FixLine()

inputFile.eachLine{line ->
    // 先fix SSL Record， 再fix SSL，这样简单一些
    line = fixLine.fixSSLRecord(line)
    line = fixLine.fixSSL(line)
    fixJsonFile << line
    fixJsonFile << "\n"
}


def dispatcher = new Dispatcher();

def inputJson = new JsonSlurper().parseText(fixJsonFile.text)
    .each{
    dispatcher.dispatch(it)
}

dispatcher.calculate()

println "End -----"
