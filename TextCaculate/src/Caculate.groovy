def logName = '100.ocsp.05.25.14.48.txt'
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

