package jp.co.cos_mos.mdm.ws.controller;

import jp.co.cos_mos.mdm.core.service.SequenceNumberService;
import jp.co.cos_mos.mdm.core.service.domain.SequenceNumberServiceRequest;
import jp.co.cos_mos.mdm.core.service.domain.SequenceNumberServiceResponse;
import jp.co.cos_mos.mdm.core.service.domain.entity.Control;
import jp.co.cos_mos.mdm.core.service.domain.entity.SequenceNumberCriteriaObj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sequence")
public class SequenceNumberServiceController {

	@Autowired
	private SequenceNumberService service;
	
	@RequestMapping(value = "numbering/{id}", method = RequestMethod.PUT)
	public @ResponseBody SequenceNumberServiceResponse numbering(@PathVariable String id) {
		Control control = new Control();
		
		SequenceNumberCriteriaObj criteria = new SequenceNumberCriteriaObj();
		criteria.setId(id);
		
		SequenceNumberServiceRequest request = new SequenceNumberServiceRequest();
		request.setCriteria(criteria);
		request.setControl(control);
		
		return service.numbering(request);
		
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody SequenceNumberServiceResponse create(@RequestBody SequenceNumberServiceRequest request) {
		Control control = request.getControl();
		control.setTransactionId(1L);
		
		return service.createCategory(request);
	}

	@RequestMapping(value = "/reset/{id}", method = RequestMethod.PUT)
	public @ResponseBody SequenceNumberServiceResponse reset(@PathVariable String id) {
		Control control = new Control();
		control.setTransactionId(1L);
		
		SequenceNumberCriteriaObj criteria = new SequenceNumberCriteriaObj();
		criteria.setId(id);
		
		SequenceNumberServiceRequest request = new SequenceNumberServiceRequest();
		request.setCriteria(criteria);
		request.setControl(control);

		return service.reset(request);
	}

}
