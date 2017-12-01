package org.libermundi.theorcs.domain.jpa.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.libermundi.theorcs.domain.jpa.base.Identity;

import javax.persistence.Entity;
import java.util.Date;

@Setter
@Getter
@ToString(of = {"username","series","tokenValue","date"})
@NoArgsConstructor
@Entity
public class RememberMeToken extends Identity {

	private String username;
    private String series;
    private String tokenValue;
    private Date date;
    
    public RememberMeToken(String username, String series, String tokenValue, Date date) {
        this.username = username;
        this.series = series;
        this.tokenValue = tokenValue;
        this.date = new Date(date.getTime());
    }

}