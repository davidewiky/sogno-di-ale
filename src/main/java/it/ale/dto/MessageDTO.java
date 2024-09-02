package it.ale.dto;

public class MessageDTO {

    private String text;

    public MessageDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
