//def logName = '100.ocsp.05.25.14.48.txt'
//def logName = 'https.1000x2.normal.and.ocsp.host.api.meipai.com.txt'
def logName = '2000x2.05.27.17.14.txt'
def baseDir = "./../res/"


def logFile = new File(baseDir + logName)

def listTitle

def dispatcher = new Dispatcher()

logFile.eachLine{line ->
    if (!listTitle) {
        listTitle = line.split(' +')
    } else {
        def map = FixLine.spliteLine(listTitle, line)
        dispatcher.dispatch(map)
    }
}

dispatcher.printInfo()

println '------- -------- end'