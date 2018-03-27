package com.example.demobatch.realestate;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class RealEstateFieldSetMapper implements FieldSetMapper<RealEstate> {
	@Override
	public RealEstate mapFieldSet(FieldSet fieldSet) throws BindException {
		RealEstate realEstate = new RealEstate();
		realEstate.setStreet(fieldSet.readString("street"));
		realEstate.setCity(fieldSet.readString("city"));
		realEstate.setZip(fieldSet.readString("zip"));
		realEstate.setState(fieldSet.readString("state"));
		realEstate.setBeds(fieldSet.readString("beds"));
		realEstate.setBaths(fieldSet.readString("baths"));
		realEstate.setSqFt(fieldSet.readString("sq__ft"));
		realEstate.setType(fieldSet.readString("type"));
		realEstate.setSaleDate(fieldSet.readString("sale_date"));
		realEstate.setPrice(fieldSet.readInt(9));
		realEstate.setLatitude(fieldSet.readDouble(10));
		realEstate.setLongitude(fieldSet.readDouble(11));
		return realEstate;
	}
}
