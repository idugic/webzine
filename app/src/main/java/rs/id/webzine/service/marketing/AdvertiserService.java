package rs.id.webzine.service.marketing;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.entity.marketing.Advertiser;
import rs.id.webzine.entity.system.Address;
import rs.id.webzine.service.GenericService;
import rs.id.webzine.service.system.AddressService;

@Component
public class AdvertiserService extends GenericService<Advertiser> {

  @Autowired
  AddressService addressService;

  @Transactional
  public void create(Advertiser advertiser, Address address) {
    addressService.create(address);
    advertiser.setAddress(address);

    create(advertiser);
  }

  @Transactional
  public void update(Integer advertiserId, Advertiser advertiserValues, Address addressValues) {

    Advertiser targetAdvertiser = find(advertiserId);

    targetAdvertiser.setName(advertiserValues.getName());
    targetAdvertiser.setDescription(advertiserValues.getDescription());

    // change address only if provided
    if (addressValues != null) {
      if (targetAdvertiser.getAddress() != null) {
        Address targetAddress = targetAdvertiser.getAddress();
        targetAddress.setEmail(addressValues.getEmail());
        targetAddress.setPhone(addressValues.getPhone());
        targetAddress.setStreetLine(addressValues.getStreetLine());
        targetAddress.setCity(addressValues.getCity());
        targetAddress.setPostalCode(addressValues.getPostalCode());
        targetAddress.setCountry(addressValues.getCountry());
        targetAddress.setCountryCode(addressValues.getCountryCode());
        targetAddress.setWww(addressValues.getWww());
      } else {
        addressService.create(addressValues);
        targetAdvertiser.setAddress(addressValues);
      }
    }

    update(targetAdvertiser);
  }

  public Advertiser findForName(String name) {
    Advertiser advertiser = null;

    if (name != null) {
      TypedQuery<Advertiser> query = entityManager().createQuery(
          "SELECT o FROM Advertiser o WHERE o.name = :advertiserName", Advertiser.class);
      query.setParameter("advertiserName", name);

      List<Advertiser> advertiserList = query.getResultList();
      if (!advertiserList.isEmpty()) {
        advertiser = advertiserList.get(0);
      }
    }

    return advertiser;
  }
}
