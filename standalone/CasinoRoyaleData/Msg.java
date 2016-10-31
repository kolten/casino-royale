package CasinoRoyaleData;

public final class Msg {

    public int student_ID_number;
    public java.lang.String name = "";
    public int userID;
    public java.lang.String message = "";

    public Msg() {
    }

    public Msg(
        int _student_ID_number,
        java.lang.String _name,
        int _userID,
        java.lang.String _message)
    {
        student_ID_number = _student_ID_number;
        name = _name;
        userID = _userID;
        message = _message;
    }

}
