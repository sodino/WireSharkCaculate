/**
 * Created by sodino on 2017/5/22.
 */
class NetJob {
    NetPackage tcp1, tcp2, tcp3
    NetPackage clientHello;

    // ======== CRL ============
    // Full Job
    // Certificate, Server Key Exchange, Server Hello Done
    // Client Key Exchange, Change Cipher Spec, Encrypted Handshake Message
    // New Session Ticket, Change Cipher Spec, Encrypted Handshake Message

    // Optimization Job
    // Server Hello, Change Cipher Spec, Encrypted Handshake Message
    // Change Cipher Spec, Encrypted Handshake Message
    // ======== CRL ============

    // ======== OCSP ============

    // ======== OCSP ============

    NetPackage certificate;
    NetPackage certificateStatus
    NetPackage serverKeyExchange
    NetPackage newSessionTicket
    NetPackage serverHello
    NetPackage serverHelloDone
    NetPackage clientKeyExchange
    NetPackage changeCipherSpec_c // client
    NetPackage changeCipherSpec_s // server
    NetPackage finished_c // client Encrypted Handshake Message
    NetPackage finished_s // server Encrypted Handshake Message



    NetPackage reqAppData;
    NetPackage rspAppData;

    void add(NetPackage pkg) {
        if (NetPackage.TCP_1.equals(pkg.type) && tcp1 == null) {
            tcp1 = pkg
        } else if (NetPackage.TCP_2.equals(pkg.type) && tcp2 == null) {
            tcp2 = pkg
        } else if (NetPackage.TCP_3.equals(pkg.type) && tcp3 == null) {
            tcp3 = pkg
        } else if (NetPackage.Client_Hello.equals(pkg.type) && clientHello == null) {
            clientHello = pkg
        }

        if (pkg.type.contains(NetPackage.New_Session_Ticket) && newSessionTicket == null) {
            newSessionTicket = pkg
        }
        if (pkg.type.contains(NetPackage.Certificate_Status) && certificateStatus == null) {
            certificateStatus = pkg
        }
        if (pkg.type.contains(NetPackage.SERVER_HELLO) && serverHello == null){
            serverHello = pkg
        }
        if (pkg.type.contains(NetPackage.Certificate) && certificate == null) {
            certificate = pkg
        }
        if (pkg.type.contains(NetPackage.ServerKeyExchange) && serverKeyExchange == null) {
            serverKeyExchange = pkg
        }
        if (pkg.type.contains(NetPackage.ServerHelloDone) && serverHelloDone == null) {
            serverHelloDone = pkg
        }
        if (pkg.type.contains(NetPackage.ClientKeyExchange) && clientKeyExchange == null) {
            clientKeyExchange = pkg
        }
        if (pkg.type.contains(NetPackage.ChangeCipherSpec)) {
            if (Constant.LOCAL_IP.equals(pkg.ip_src)) {
                if (changeCipherSpec_c == null) {
                    changeCipherSpec_c = pkg
                }
            } else {
                if (changeCipherSpec_s == null) {
                    changeCipherSpec_s = pkg
                }
            }
        }
        if (pkg.type.contains(NetPackage.FINISHED)) {
            if (Constant.LOCAL_IP.equals(pkg.ip_src)) {
                if (finished_c == null) {
                    finished_c = pkg
                }
            } else {
                if (finished_s == null) {
                    finished_s = pkg
                }
            }
        }
        if (NetPackage.APP_DATA.equals(pkg.type)) {
            if (Constant.LOCAL_IP.equals(pkg.ip_src)) {
                if (reqAppData == null) {
                    reqAppData = pkg
                }
            } else {
                if (rspAppData == null) {
                    rspAppData = pkg
                }
            }
        }
    }
}
