package gen.provider;

import java.util.ArrayList;
import java.util.List;

import gen.general.Obj;
import gen.service.Service;

public class Provider extends Obj {
	List<Service> services;

	public Provider() {
		super();
		services = new ArrayList<>();
	}

	public Provider(String id, String type) {
		super(id, type);
		services = new ArrayList<>();
	}

	public List<Service> getServices() {
		return services;
	}


	public void addService(Service s) {
		services.add(s);
	}

	

}
