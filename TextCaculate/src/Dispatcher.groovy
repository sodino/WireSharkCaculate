/**
 * Created by sodino on 2017/5/26.
 */
class Dispatcher {

    def mapHost = [:] // [host_ip : httpsRequestList]
    def reqCurrent
    def dispatch(LinkedHashMap map) {
        NetPackage pkg = new NetPackage(map)
        if (reqCurrent == null) {
            reqCurrent = new HttpsRequest()
        } else if (reqCurrent.isFIN || reqCurrent.isRST) {
            def host = reqCurrent.ip_server
            add2HostRecord(host, reqCurrent)

            reqCurrent = new HttpsRequest()
        }

        reqCurrent.add(pkg)
    }


    def add2HostRecord(String host, HttpsRequest req) {
        def arr = mapHost[host]
        if (arr == null) {
            arr = []
            mapHost.put(host, arr)
        }
        arr << req

     //   println "mapHost.size = ${mapHost.size()} host[$host] size = ${arr.size()}"
    }

    def printInfo() {
        mapHost.each { host, arr ->
            println "Server : $host"

            printRequestArray(arr)

            println '------------------------- \n'
        }
    }

    def printRequestArray(ArrayList<HttpsRequest> arr) {
//        println 'No.\t\tTCP1\tTCP2\tTCP3\tC_H\t\tS_H\t\tCer\t\tC_S\t\tSKE\t\tSHD\t\tCKE\t\tCC_c\tNST\t\tCC_s\tAD_c\tAD_s\tAll '
        print String.format(Constant.FORMAT_STRING, "Idx")
        print String.format(Constant.FORMAT_STRING, "NO.")
        print String.format(Constant.FORMAT_STRING, "TCP1")
        print String.format(Constant.FORMAT_STRING, "TCP2")
        print String.format(Constant.FORMAT_STRING, "TCP3")
        print String.format(Constant.FORMAT_STRING, "C_H")
        print String.format(Constant.FORMAT_STRING, "S_H")
        print String.format(Constant.FORMAT_STRING, "Cer")
        print String.format(Constant.FORMAT_STRING, "C_S")
        print String.format(Constant.FORMAT_STRING, "SKE")
        print String.format(Constant.FORMAT_STRING, "SHD")
        print String.format(Constant.FORMAT_STRING, "CKE")
        print String.format(Constant.FORMAT_STRING, "CC_c")
        print String.format(Constant.FORMAT_STRING, "NST")
        print String.format(Constant.FORMAT_STRING, "CC_s")
        print String.format(Constant.FORMAT_STRING, "AD_c")
        print String.format(Constant.FORMAT_STRING, "AD_s")
        print String.format(Constant.FORMAT_STRING, "All")
        println ''

        def arrStep = []
        arrStep << Constant.Handshake.TCP_1 // tcp1提前打印了
        arrStep << Constant.Handshake.TCP_2
        arrStep << Constant.Handshake.TCP_3
        arrStep << Constant.Handshake.Client_Hello
        arrStep << Constant.Handshake.Server_Hello
        arrStep << Constant.Handshake.Certificate
        arrStep << Constant.Handshake.Certificate_Status
        arrStep << Constant.Handshake.Server_Key_Exchange
        arrStep << Constant.Handshake.Server_Hello_Done
        arrStep << Constant.Handshake.Client_Key_Exchange
        arrStep << Constant.Handshake.Change_Cipher_Spec_c
        arrStep << Constant.Handshake.New_Session_Ticket
        arrStep << Constant.Handshake.Change_Cipher_Spec_s
        arrStep << Constant.Handshake.Applicateion_Data_c
        arrStep << Constant.Handshake.Applicateion_Data_s
        arrStep << Constant.Handshake.All

        def sumTime = new SumTime()

        def index = 0
        arr.forEach{ req ->
            req.printData(index, arrStep, sumTime)
            index ++
        }

        sumTime.printAverage(arrStep)
    }
}
