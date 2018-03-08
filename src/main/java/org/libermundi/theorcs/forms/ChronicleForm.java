package org.libermundi.theorcs.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.jpa.chronicle.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"title"},callSuper = true)
public class ChronicleForm {

    @NotNull
    @Size(max = 50)
    private String title;

    @Size(max = 50)
    private String subTitle;

    private String description;

    private ChronicleType type;

    private ChronicleGenre genre;

    private ChronicleStyle style;

    private ChroniclePace pace;

    @Size(max=15)
    private String password;

    @Size(max=50)
    private String date;

    private String background;

    private Game game;

}
