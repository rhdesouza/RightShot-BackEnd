package rightShot.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import rightShot.entity.User;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User principal;

		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().toString().equals("anonymousUser")) {
			return Optional.ofNullable("RIGHTSHOT");
		} else {
			principal = (User) authentication.getPrincipal();
		}

		return Optional.ofNullable(principal.getUser());
		/* return Optional.ofNullable((User) authentication.getPrincipal()); */

		/* return ((MyUserDetails) authentication.getPrincipal()).getUser(); */
	}
}
