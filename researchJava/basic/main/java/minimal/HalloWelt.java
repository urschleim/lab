package minimal;

import javax.jws.*;

@WebService
public interface HalloWelt
{
   public String hallo( @WebParam( name = "wer" ) String wer );
}
