import java.text.DecimalFormat

/**
 * Created by sodino on 2017/5/26.
 */
class Constant {
    static final String NO = 'No.'
    static final String TIME = 'Time'
    static final String SOURCE = 'Source'
    static final String DESTINATION = 'Destination'
    static final String PROTOCOL = 'Protocol'
    static final String INFO = 'Info'
    static def Data_Format = new DecimalFormat('0.000')

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
    }
}
