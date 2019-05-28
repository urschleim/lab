package minimal;

import javax.jws.WebService;

@WebService( endpointInterface="minimal.HalloWelt" )
public class HalloWeltImpl implements HalloWelt
{
   public String hallo( String wer )
   {
      return "Hallo " + wer;
   }
}