
package com.netsuite.webservices.platform.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpdateInviteeStatusResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateInviteeStatusResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:messages_2014_2.platform.webservices.netsuite.com}writeResponse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateInviteeStatusResponse", propOrder = {
    "writeResponse"
})
public class UpdateInviteeStatusResponse {

    @XmlElement(required = true)
    protected WriteResponse writeResponse;

    /**
     * Gets the value of the writeResponse property.
     * 
     * @return
     *     possible object is
     *     {@link WriteResponse }
     *     
     */
    public WriteResponse getWriteResponse() {
        return writeResponse;
    }

    /**
     * Sets the value of the writeResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link WriteResponse }
     *     
     */
    public void setWriteResponse(WriteResponse value) {
        this.writeResponse = value;
    }

}