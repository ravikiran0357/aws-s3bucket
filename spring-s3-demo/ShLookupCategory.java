package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the sh_lookup_categories database table.
 * 
 */
@Entity
@Table(name="sh_lookup_categories")
@NamedQuery(name="ShLookupCategory.findAll", query="SELECT s FROM ShLookupCategory s")
public class ShLookupCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="sh_lookup_catg_id")
	private int shLookupCatgId;

	@Column(name="catg_desc")
	private String catgDesc;

	@Column(name="catg_name")
	private String catgName;

	@Column(name="created_by")
	private int createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_on")
	private Date createdOn;

	@Column(name="modified_by")
	private int modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="modified_on")
	private Date modifiedOn;

	private byte status;

	//bi-directional many-to-one association to ShLookup
	@OneToMany(mappedBy="shLookupCategory")
	private List<ShLookup> shLookups;

	public ShLookupCategory() {
	}

	public int getShLookupCatgId() {
		return this.shLookupCatgId;
	}

	public void setShLookupCatgId(int shLookupCatgId) {
		this.shLookupCatgId = shLookupCatgId;
	}

	public String getCatgDesc() {
		return this.catgDesc;
	}

	public void setCatgDesc(String catgDesc) {
		this.catgDesc = catgDesc;
	}

	public String getCatgName() {
		return this.catgName;
	}

	public void setCatgName(String catgName) {
		this.catgName = catgName;
	}

	public int getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public int getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(int modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedOn() {
		return this.modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public List<ShLookup> getShLookups() {
		return this.shLookups;
	}

	public void setShLookups(List<ShLookup> shLookups) {
		this.shLookups = shLookups;
	}

	public ShLookup addShLookup(ShLookup shLookup) {
		getShLookups().add(shLookup);
		shLookup.setShLookupCategory(this);

		return shLookup;
	}

	public ShLookup removeShLookup(ShLookup shLookup) {
		getShLookups().remove(shLookup);
		shLookup.setShLookupCategory(null);

		return shLookup;
	}

}