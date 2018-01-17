package org.libermundi.theorcs.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class MessageForm {

    @NotNull
    private Character from;

    private Set<Character> to;

    private Set<Character> cc;

    private Set<Character> bcc;

    @NotNull
    @Size(max = 255)
    private String subject;

    @NotNull
    private String content;

    private boolean anonymous;

    private boolean signature;

}
