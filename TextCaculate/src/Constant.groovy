import java.text.DecimalFormat

/**
 * Created by sodino on 2017/5/26.
 */
class Constant {
    static final String NO = 'No.'
    static final String TIME = 'Time'
    static final String LENGTH = 'Length'
    static final String SOURCE = 'Source'
    static final String DESTINATION = 'Destination'
    static final String PROTOCOL = 'Protocol'
    static final String INFO = 'Info'
    static final String FORMAT_INT = "% 5d"
    static final String FORMAT_FLOAT = "% 5f"
    static final String FORMAT_STRING = "%11s"
    static def Data_Format = new DecimalFormat('0.00000')

    class Protocol {
        public static final String TCP = 'TCP'
        public static final String TLS = 'TLS'
    }

    class Handshake {
        public static final String TCP_1 = '[SYN] Seq=0'
        public static final String TCP_2 = '[SYN, ACK] Seq=0 Ack=1'
        public static final String TCP_3 = '[ACK] Seq=1'
        public static final String Client_Hello = 'Client Hello'
        public static final String Server_Hello = 'Server Hello'
        public static final String Certificate = 'Certificate'
        public static final String Certificate_Status = 'Certificate Status'
        public static final String Server_Key_Exchange = 'Server Key Exchange'
        public static final String Server_Hello_Done = 'Server Hello Done'
        public static final String Client_Key_Exchange = 'Client Key Exchange'
        public static final String New_Session_Ticket = 'New Session Ticket'
        public static final String Change_Cipher_Spec = 'Change Cipher Spec'
//        public static final String Encrypted_Handshake_Message = 'Encrypted Handshake Message'
        public static final String Applicateion_Data = 'Application Data'

        public static final String FIN = 'FIN'

        public static final String RST = 'RST'


        public static final String Change_Cipher_Spec_c = 'Change Cipher Spec from client'
        public static final String Change_Cipher_Spec_s = 'Change Cipher Spec from server'
        public static final String Applicateion_Data_c = 'Application Data from client'
        public static final String Applicateion_Data_s = 'Application Data from server'

        public static final String All = 'Tcp1 to AppData_s'
    }

    class Pattern {
        public static final def TCP_1 = /\[SYN(.*)\] Seq=0/
        public static final def TCP_2 = /\[SYN(.*)ACK(.*)\] Seq=0 Ack=1/
        public static final def TCP_3 = /\[(.*)ACK(.*)\] Seq=1 Ack=1/
        public static final def FIN = /\[FIN(.*)\]/
        public static final def RST = /\[RST(.*)\]/
    }

    class TCP {
        public static final int TCP_1 = 1
        public static final int TCP_2 = 2
        public static final int TCP_3 = 3

        public static final int RST = Integer.MAX_VALUE -1
        public static final int FIN = Integer.MAX_VALUE
    }
}
