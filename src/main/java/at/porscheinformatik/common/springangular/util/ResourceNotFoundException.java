package at.porscheinformatik.common.springangular.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException
{

	private static final long serialVersionUID = 8354085226475403536L;

}
