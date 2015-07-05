package api;


public class BlacklistEntity {
    private String word;

    public BlacklistEntity(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
}
