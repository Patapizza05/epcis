//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.15 at 01:39:48 PM KST 
//


package org.oliot.model.oliot;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.namespace.QName;



@Entity
public class AggregationEventExtension {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	

	@OneToOne
	@JoinColumn(name="childQuantityList_id")
    protected QuantityList childQuantityList;
	@OneToOne
	@JoinColumn(name="sourceList_id")	
    protected SourceList sourceList;
	
	@OneToOne
	@JoinColumn(name="destinationList_id")	
    protected DestinationList destinationList;
    
    
    
	@OneToOne
	@JoinColumn(name="AggregationEventExtension2_id")
    protected AggregationEventExtension2 extension;
    
    @Transient
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	public QuantityList getChildQuantityList() {
		return childQuantityList;
	}

	public void setChildQuantityList(QuantityList childQuantityList) {
		this.childQuantityList = childQuantityList;
	}

	public SourceList getSourceList() {
		return sourceList;
	}

	public void setSourceList(SourceList sourceList) {
		this.sourceList = sourceList;
	}

	public DestinationList getDestinationList() {
		return destinationList;
	}

	public void setDestinationList(DestinationList destinationList) {
		this.destinationList = destinationList;
	}

	public AggregationEventExtension2 getExtension() {
		return extension;
	}

	public void setExtension(AggregationEventExtension2 extension) {
		this.extension = extension;
	}

	public Map<QName, String> getOtherAttributes() {
		return otherAttributes;
	}

	public void setOtherAttributes(Map<QName, String> otherAttributes) {
		this.otherAttributes = otherAttributes;
	}

	

 

}
