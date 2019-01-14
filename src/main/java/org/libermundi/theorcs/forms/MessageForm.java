package org.libermundi.theorcs.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class MessageForm {

    @NotNull
    private Character from;

    private Set<Character> to = new HashSet<>();

    private Set<Character> cc = new HashSet<>();

    private Set<Character> bcc = new HashSet<>();

    @NotNull
    @Size(max = 255)
    private String subject;

    @NotNull
    private String content;

    private boolean anonymous = Boolean.FALSE;

    private boolean signature = Boolean.FALSE;

}
