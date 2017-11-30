package org.libermundi.theorcs.domain.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Locale;

@Getter
@Setter
@ToString(of = {"address1","address2","city"})
@Embeddable
public final class Address implements Serializable {

	@Column(length = 255)
	private String address1;

	@Column(length = 255)
	private String address2;

	@Column(length = 50)
	private String city;

	@Column(length = 10)
	private String zipcode;

	@Column(length = 50)
	private String state;

	@Column(length = 2)
	private String countryIso;

}
