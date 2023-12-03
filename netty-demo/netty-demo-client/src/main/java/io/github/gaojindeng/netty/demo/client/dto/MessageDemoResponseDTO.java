package io.github.gaojindeng.netty.demo.client.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author gjd
 */
@Data
@NoArgsConstructor
public class MessageDemoResponseDTO implements Serializable {

    private String value;

    public MessageDemoResponseDTO(String value) {
        this.value = value;
    }
}
