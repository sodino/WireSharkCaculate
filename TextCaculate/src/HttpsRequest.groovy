/**
 * Created by sodino on 2017/5/26.
 */
class HttpsRequest {
    def map = [:]
    def isFIN = false
    def isRST = false
    def isOCSP = false
    def line_number // tcp1 line number
    def isSessionResumption = false
    def ip_client = ''
    def ip_server = ''

    def add(NetPackage pkg){
        if (isFIN) {
            return false
        }

        def key = Constant.Handshake.TCP_1
        def isTcp1 = pkg.isTCP(Constant.TCP.TCP_1)

        if (map[key] == null) {
            if (isTcp1 == false) {
                // 限定第一个填充的一定要是tcp1
                return
            }
        }


        def preSize = map.size()

        if (isTcp1 && map[key] == null) {
            map.put(key, pkg)
            ip_client = pkg.getIPSrc()
            ip_server = pkg.getIPDst()
            line_number = pkg.number
        }





        key = Constant.Handshake.TCP_2
        if (pkg.isTCP(Constant.TCP.TCP_2) && map[key] == null) {
            map.put(key, pkg)
        }

        key = Constant.Handshake.TCP_3
        if (pkg.isTCP(Constant.TCP.TCP_3) && map[key] == null) {
            map.put(key, pkg)
        }

        key = Constant.Handshake.FIN
        if (pkg.isTCP(Constant.TCP.FIN) && map[key] == null) {
            map.put(key, pkg)
        }

        key = Constant.Handshake.RST
        if (pkg.isTCP(Constant.TCP.RST) && map[key] == null) {
            map.put(key, pkg)
        }

        key = Constant.Handshake.Client_Hello
        if (pkg.isClientHello() && map[key] == null) {
            map.put(key, pkg)
        }

        key = Constant.Handshake.Server_Hello
        if (pkg.isServerHello() && map[key] == null) {
            map.put(key, pkg)
        }

        key = Constant.Handshake.Certificate
        if (pkg.isCertificate() && map[key] == null) {
            map.put(key, pkg)
        }

        key = Constant.Handshake.Certificate_Status
        if (pkg.isCertificateStatus() && map[key] == null) {
            map.put(key, pkg)
            isOCSP = true
        }

        key = Constant.Handshake.Server_Key_Exchange
        if (pkg.isServerKeyExchange() && map[key] == null) {
            map.put(key, pkg)
        }

        key = Constant.Handshake.Server_Hello_Done
        if (pkg.isServerHelloDone() && map[key] == null) {
            map.put(key, pkg)
        }

        key = Constant.Handshake.Client_Key_Exchange
        if (pkg.isClientKeyExchange() && map[key] == null) {
            map.put(key, pkg)
        }

        key = Constant.Handshake.New_Session_Ticket
        if (pkg.isNewSessionTicket() && map[key] == null) {
            map.put(key, pkg)
            isSessionResumption = true
        }


        if (pkg.isChangeCipherSpec()) {
            if (isFromClient(pkg)) {
                key = Constant.Handshake.Change_Cipher_Spec_c
                map.put(key, pkg)
            } else if (isFromServer(pkg)) {
                key = Constant.Handshake.Change_Cipher_Spec_s
                map.put(key, pkg)
            }
        }

        if (pkg.isApplicateionData()) {
            if (isFromClient(pkg)) {
                key = Constant.Handshake.Applicateion_Data_c
                map.put(key, pkg)
            } else if (isFromServer(pkg)) {
                key = Constant.Handshake.Applicateion_Data_s
                map.put(key, pkg)
            }
        }


        if (map[Constant.Handshake.FIN]){
            isFIN = true
        }

        if (map[Constant.Handshake.RST]) {
            isRST = true
        }

        def size = map.size()
        if (size > preSize) {
            return true
        } else {
            return false
        }
    }

    def isFromClient(NetPackage pkg) {
        if (ip_client.equals(pkg.getIPSrc())) {
            true
        } else {
            false
        }
    }

    def isFromServer(NetPackage pkg) {
        if (ip_server.equals(pkg.getIPSrc())) {
            true
        } else {
            false
        }
    }

    def isRST() {
        isRST
    }

    def isFIN() {
        isFIN
    }

    def isOCSP() {
        isOCSP
    }

    def isSessionResumption() {
        isSessionResumption
    }

    def printData(int index, ArrayList<String> arrStep, SumTime sumTime) {

        def format = Constant.Data_Format

        def value
        def pre = map[Constant.Handshake.TCP_1]
        def suf

        print String.format(Constant.FORMAT_STRING, index)    // index
        print String.format(Constant.FORMAT_STRING, line_number) // No.
        print String.format(Constant.FORMAT_STRING, '0.000')  // tcp1

        // tcp2 ... ...
        arrStep.forEach{ step ->
            if (Constant.Handshake.TCP_1.equals(step)) {
                return
            } else if (Constant.Handshake.All.equals(step)) {

                if (map[Constant.Handshake.Applicateion_Data_s] != null) {
                    def all = (map[Constant.Handshake.Applicateion_Data_s].getTime() - map[Constant.Handshake.TCP_1].getTime()) * 1000

                    sumTime.add(step, all)

//                    print format.format(all)
//                    print String.format(Constant.FORMAT_FLOAT, all)
                    print String.format(Constant.FORMAT_STRING, format.format(all))
                } else {
                    print 'x.xxx'
                }
//                print '\t'
                return
            }
            suf = map[step]

            if (suf == null) {
                print String.format(Constant.FORMAT_STRING, 'x.xxx')
            } else if (suf == pre) {
                print String.format(Constant.FORMAT_STRING, '<---')
            } else {
                def tSuf = suf.getTime()
                def tPre = pre.getTime()
                value = (tSuf - tPre) * 1000

                // ocsp开启Session Resumption后，Client Cipher Spec会先Server 后 Client导致负数
                value = Math.abs(value)

                sumTime.add(step, value)

//                print format.format(value)
//                print String.format(Constant.FORMAT_FLOAT, value)
                print String.format(Constant.FORMAT_STRING, format.format(value))
            }

            pre = (suf == null ? pre : suf)
        }

        println ''
    }
}
