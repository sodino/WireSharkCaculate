import java.text.DecimalFormat

/**
 * Created by sodino on 2017/5/26.
 */
class HttpsRequest {
    def map = [:]
    def isFull = false
    def isOCSP = false
    def line_number // tcp1 line number
    def isSessionResumption = false
    def ip_client = ''
    def ip_server = ''

    def add(NetPackage pkg){
        if (isFull) {
            return false
        }

        def key = Constant.Handshake.TCP_1
        def isTcp1 = pkg.isTCP(1)

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
        if (pkg.isTCP(2) && map[key] == null) {
            map.put(key, pkg)
        }

        key = Constant.Handshake.TCP_3
        if (pkg.isTCP(3) && map[key] == null) {
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
            } else if (isFromServer(pkg)) {
                key = Constant.Handshake.Change_Cipher_Spec_s
            }

            if (map[key] == null) {
                map.put(key, pkg)
            }
        }

        if (pkg.isApplicateionData()) {
            if (isFromClient(pkg)) {
                key = Constant.Handshake.Applicateion_Data_c
            } else if (isFromServer(pkg)) {
                key = Constant.Handshake.Applicateion_Data_s
            }
            if (map[key] == null) {
                map.put(key, pkg)
            }
        }


        if (map[Constant.Handshake.Applicateion_Data_s] != null){
            isFull = true
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

    def isFull() {
        isFull
    }

    def isOCSP() {
        isOCSP
    }

    def isSessionResumption() {
        isSessionResumption
    }

    def printData(ArrayList<String> arrStep, SumTime sumTime) {

        def format = Constant.Data_Format

        def value
        def pre = map[Constant.Handshake.TCP_1]
        def suf

        print line_number + '\t' // No.
        print '0.000' + '\t' // tcp1

        // tcp2 ... ...
        arrStep.forEach{ step ->
            if (Constant.Handshake.TCP_1.equals(step)) {
                return
            } else if (Constant.Handshake.All.equals(step)) {

                def all = (map[Constant.Handshake.Applicateion_Data_s].getTime() - map[Constant.Handshake.TCP_1].getTime()) * 1000

                sumTime.add(step, all)

                print format.format(all)
                print '\t'
                return
            }
            suf = map[step]

            if (suf == null) {
                print 'x.xxx'
            } else if (suf == pre) {
                print '<---'
            } else {
                def tSuf = suf.getTime()
                def tPre = pre.getTime()
                value = (tSuf - tPre) * 1000

                sumTime.add(step, value)

                print format.format(value)
            }
            print '\t'

            pre = (suf == null ? pre : suf)
        }

        println ''


    }
}
