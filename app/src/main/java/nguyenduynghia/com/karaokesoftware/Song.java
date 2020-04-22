package nguyenduynghia.com.karaokesoftware;

public class Song {
    private String ma;
    private String ten;
    private String casi;
    private int love;

    public Song() {
    }

    public Song(String ma, String ten, String casi, int love) {
        this.ma = ma;
        this.ten = ten;
        this.casi = casi;
        this.love = love;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getCasi() {
        return casi;
    }

    public void setCasi(String casi) {
        this.casi = casi;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }
}
