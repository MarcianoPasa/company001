package com.system.company001.models;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name = "TB_CUSTOMERS")
public class CustomerModel extends RepresentationModel<CustomerModel> implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID idCustomer;
	private String businessName;  //Nome fantasia
	private String corporateName; //Razão social
	private String businessTaxId; //CNPJ no Brasil

	public UUID getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(UUID idCustomer) {
		this.idCustomer = idCustomer;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getCorporateName() {
		return corporateName;
	}

	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}

	public String getBusinessTaxId() {
		return businessTaxId;
	}

	public void setBusinessTaxId(String businessTaxId) {
		this.businessTaxId = businessTaxId;
	}
}
