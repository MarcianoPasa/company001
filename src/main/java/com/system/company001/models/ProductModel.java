package com.system.company001.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TB_PRODUCTS")
public class ProductModel implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID idProduct;

	private String name;

	private BigDecimal value;

	@Lob
	@JdbcType(VarbinaryJdbcType.class)
	@Column(name = "image", columnDefinition = "bytea")
	private byte[] image;

	@Lob
	@JdbcType(VarbinaryJdbcType.class)
	@Column(name = "thumbnail", columnDefinition = "bytea")
	private byte[] thumbnail;

	public UUID getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(UUID idProduct) {
		this.idProduct = idProduct;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof ProductModel other)) {
			return false;
		}

		return idProduct != null && idProduct.equals(other.getIdProduct());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
