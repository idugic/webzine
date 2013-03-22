package rs.id.webzine.web;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import rs.id.webzine.domain.Address;
import rs.id.webzine.domain.Country;
import rs.id.webzine.domain.Customer;
import rs.id.webzine.domain.UserProfile;

@RequestMapping("/addresses")
@Controller
public class AddressController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Address address, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, address);
			return "addresses/create";
		}
		uiModel.asMap().clear();
		address.persist();
		return "redirect:/addresses/"
				+ encodeUrlPathSegment(address.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Address());
		return "addresses/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		uiModel.addAttribute("address", Address.findAddress(id));
		uiModel.addAttribute("itemId", id);
		return "addresses/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			uiModel.addAttribute("addresses",
					Address.findAddressEntries(firstResult, sizeNo));
			float nrOfPages = (float) Address.countAddresses() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("addresses", Address.findAllAddresses());
		}
		return "addresses/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Address address, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, address);
			return "addresses/update";
		}
		uiModel.asMap().clear();
		address.merge();
		return "redirect:/addresses/"
				+ encodeUrlPathSegment(address.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		populateEditForm(uiModel, Address.findAddress(id));
		return "addresses/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		Address address = Address.findAddress(id);
		address.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/addresses";
	}

	void populateEditForm(Model uiModel, Address address) {
		uiModel.addAttribute("address", address);
		uiModel.addAttribute("countries", Country.findAll());
		uiModel.addAttribute("customers", Customer.findAllCustomers());
		uiModel.addAttribute("userprofiles", UserProfile.findAllUserProfiles());
	}

	String encodeUrlPathSegment(String pathSegment,
			HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}
}
