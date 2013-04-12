package rs.id.webzine.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rs.id.webzine.domain.Customer;
import rs.id.webzine.domain.system.Address;
import rs.id.webzine.web.backing.CustomerBacking;

@RequestMapping("admin/customer")
@Controller
public class CustomerController extends WebController {

  private static final Log log = LogFactory.getLog(CustomerController.class);

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel) {
    populateEditForm(uiModel, new CustomerBacking());
    return "admin/customer/create";
  }

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(@Valid CustomerBacking customerBacking, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    try {
      // bind
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, customerBacking);
        return "admin/customer/create";
      }

      // address
      Address address = new Address();
      PropertyUtils.copyProperties(address, customerBacking);
      // address.persist();

      // persist
      Customer customer = new Customer();
      PropertyUtils.copyProperties(customer, customerBacking);

      customer.setAddressId(address);
      customer.persist();

      uiModel.asMap().clear();
      return "redirect:/admin/customer/" + encodeUrlPathSegment(customer.getId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
    try {
      CustomerBacking customerBacking = new CustomerBacking();
      Customer customer = Customer.find(id);
      PropertyUtils.copyProperties(customerBacking, customer);
      customerBacking.setBackingId(customer.getId());

      if (customer.getAddressId() != null) {
        PropertyUtils.copyProperties(customerBacking, customer.getAddressId());
      }

      populateEditForm(uiModel, customerBacking);
      return "admin/customer/update";
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(@Valid CustomerBacking customerBacking, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    try {
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, customerBacking);
        return "admin/customer/update";
      }

      Customer customer = Customer.find(customerBacking.getBackingId());

      PropertyUtils.copyProperties(customer, customerBacking);

      customer.setId(customerBacking.getBackingId());

      // address
      if (customer.getAddressId() != null) {
        PropertyUtils.copyProperties(customer.getAddressId(), customerBacking);
      } else {
        Address address = new Address();
        PropertyUtils.copyProperties(address, customerBacking);
        // address.persist();

        customer.setAddressId(address);
      }

      uiModel.asMap().clear();
      customer.merge();
      return "redirect:/admin/customer/" + encodeUrlPathSegment(customer.getId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      CustomerBacking customerBacking = new CustomerBacking();

      Customer customer = Customer.find(id);

      if (customer.getAddressId() != null) {
        PropertyUtils.copyProperties(customerBacking, customer.getAddressId());
        customerBacking.setBackingId(id);
      }
      PropertyUtils.copyProperties(customerBacking, customer);

      uiModel.addAttribute("customerBacking", customerBacking);
      uiModel.addAttribute("itemId", id);
      return "admin/customer/show";
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    try {
      List<Customer> customerList = new ArrayList<Customer>();
      if (page != null || size != null) {
        int sizeNo = size == null ? 10 : size.intValue();
        final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;

        customerList = Customer.findEntries(firstResult, sizeNo);
        float nrOfPages = (float) Customer.count() / sizeNo;
        uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
            : nrOfPages));
      } else {
        customerList = Customer.findAll();
      }

      List<CustomerBacking> customerBackingList = new ArrayList<CustomerBacking>();
      for (Customer customer : customerList) {
        CustomerBacking customerBacking = new CustomerBacking();
        PropertyUtils.copyProperties(customerBacking, customer);
        customerBacking.setBackingId(customer.getId());

        Address address = customer.getAddressId();
        if (address != null) {
          PropertyUtils.copyProperties(customerBacking, address);
        }

        customerBackingList.add(customerBacking);
      }

      uiModel.addAttribute("customerBacking", customerBackingList);

      return "admin/customer/list";
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    Customer customer = Customer.find(id);
    customer.remove();
    uiModel.asMap().clear();
    uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
    uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
    return "redirect:/admin/customer";
  }

  void populateEditForm(Model uiModel, CustomerBacking customerBacking) {
    uiModel.addAttribute("customerBacking", customerBacking);
  }

}
