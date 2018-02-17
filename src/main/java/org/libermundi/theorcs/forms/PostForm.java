package org.libermundi.theorcs.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.libermundi.theorcs.domain.jpa.chronicle.Character;
import org.libermundi.theorcs.domain.jpa.scene.Scene;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PostForm {

    @NotNull
    private Character from;

    @NotNull
    private String content;

    @NotNull
    private Scene scene;

    private Boolean registerToScene = Boolean.FALSE;
}
