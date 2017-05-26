/**
 * Created by sodino on 2017/5/26.
 */
class NetPackage {
    def map
    NetPackage(def map) {
        this.map = map

        // test(this)
    }



    def getNumber(){
        map[Constant.NO]
    }

    def getTime() {
        map[Constant.TIME]
    }

    def getIPSrc() {
        map[Constant.SOURCE]
    }

    def getIPDst() {
        map[Constant.DESTINATION]
    }

    def getProtocol() {
        map[Constant.PROTOCOL]
    }

    def getInfo() {
        map[Constant.INFO]
    }

    def isClientHello() {
        def info = map[Constant.INFO]
        if (info.equals(Constant.Handshake.Client_Hello)) {
            true
        } else {
            false
        }
    }


    def isServerHello() {
        def info = map[Constant.INFO]
        if (info.equals(Constant.Handshake.Server_Hello)) {
            true
        } else {
            false
        }
    }
    def isServerHelloDone() {
        def info = map[Constant.INFO]
        if (info.contains(Constant.Handshake.Server_Hello_Done)) {
            true
        } else {
            false
        }
    }


    def isCertificate() {
        def info = map[Constant.INFO]
        if (info.equals(Constant.Handshake.Certificate)) {
            true
        } else if (info.contains(Constant.Handshake.Certificate)
                && !info.contains(Constant.Handshake.Certificate_Status)) {
            true
        } else {
            false
        }
    }


    def isCertificateStatus() {
        def info = map[Constant.INFO]
        if (info.contains(Constant.Handshake.Certificate_Status)) {
            true
        } else {
            false
        }
    }


    def isServerKeyExchange() {
        def info = map[Constant.INFO]
        if (info.contains(Constant.Handshake.Server_Key_Exchange)) {
            true
        } else {
            false
        }
    }

    def isClientKeyExchange() {
        def info = map[Constant.INFO]
        if (info.contains(Constant.Handshake.Client_Key_Exchange)) {
            true
        } else {
            false
        }
    }



    def isChangeCipherSpec() {
        def info = map[Constant.INFO]
        if (info.contains(Constant.Handshake.Change_Cipher_Spec)) {
            true
        } else {
            false
        }
    }

    def isNewSessionTicket() {
        def info = map[Constant.INFO]
        if (info.contains(Constant.Handshake.New_Session_Ticket)) {
            true
        } else {
            false
        }
    }

    def isEncryptedHandshakeMessage() {
        def info = map[Constant.INFO]
        if (info.contains(Constant.Handshake.Encrypted_Handshake_Message)) {
            true
        } else {
            false
        }
    }


    def isApplicateionData() {
        def info = map[Constant.INFO]
        if (info.equals(Constant.Handshake.Applicateion_Data)) {
            true
        } else {
            false
        }
    }

    def isTCP(def step) {
        def isTcp = false
        def protocol = map[Constant.PROTOCOL]
        if (Constant.Protocol.TCP.equals(protocol)) {
            isTcp = true
        }

        if (step != null && isTcp) {
            def result = false
            def info = map[Constant.INFO]
            def pattern
            switch(step){
                case 1:
                    pattern = Constant.Pattern.TCP_1
                    break;
                case 2:
                    pattern = Constant.Pattern.TCP_2
                    break;
                case 3:
                    pattern = Constant.Pattern.TCP_3
                    break;
                default:
                    break;
            }

            if (pattern != null) {
                def matcher = info =~ pattern
                if (matcher.count == 1) {
                    result = true
                }
            }

            result
        } else {
            isTcp
        }
    }

    def isTLS() {
        def protocol = map[Constant.PROTOCOL]
        if (protocol.contains(Constant.Protocol.TLS)) {
            true
        } else {
            false
        }
    }


    def test(NetPackage pkg) {
        switch(pkg.number){
            case 1:
            case 2:
            case 3:
                println "line ${pkg.number} is tcp${pkg.number}:  ${pkg.isTCP(pkg.number)}"
                break;
            case 4:
                println "line ${pkg.number} is Client Hello:  ${pkg.isClientHello()}"
                break;
            case 5:
                println "line ${pkg.number} is Server Hello:  ${pkg.isServerHello()}"
                break;
            case 8:
                println "line ${pkg.number} is Certificate:  ${pkg.isCertificate()}"
                break;
            case 12:
                println "line ${pkg.number} is Certificate Status:  ${pkg.isCertificateStatus()}"
                println "line ${pkg.number} is Server Key Exchange:  ${pkg.isServerKeyExchange()}"
                println "line ${pkg.number} is Server Key Exchange:  ${pkg.isServerHelloDone()}"
                break;
            case 14:
                println "line ${pkg.number} is Client Key Exchange:  ${pkg.isClientKeyExchange()}"
                println "line ${pkg.number} is Change Cipher Spec:  ${pkg.isChangeCipherSpec()}"
                break;
            case 15:
                println "line ${pkg.number} is New Session Ticket:  ${pkg.isNewSessionTicket()}"
                println "line ${pkg.number} is Change Cipher Spec:  ${pkg.isChangeCipherSpec()}"
                println "line ${pkg.number} is Encrypted Handshake Message:  ${pkg.isEncryptedHandshakeMessage()}"
                break;
            case 17:
                println "line ${pkg.number} is Application Data:  ${pkg.isApplicateionData()}"
                break;
        }
    }


}
