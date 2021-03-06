package Main;

import javax.security.auth.login.LoginException;

import data.Affinity;
import data.Bank;
import data.SurnameStorage;
import item.InventoryManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class NadaBot {

	private static JDA jda;

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Vieulliez indiquer le token du bot");
		}
		new NadaBot(args[0]);
	}

	public NadaBot(String token) {
		SurnameStorage.initSurnmae();
		Affinity.initAfinity();
		Bank.initBank();
		InventoryManager.init();

		try {
			jda = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RateLimitedException e) {
			e.printStackTrace();
		}

		jda.addEventListener(new MessageListener());
	}
	
	public static User findMate(String user, User author){
		User found = null;
		if(jda != null){
			for( Guild g : jda.getGuilds()){
				if( g.isMember(author) )
					for( Member m : g.getMembers()){
						if(user.contains(m.getUser().getDiscriminator())){
							return m.getUser();
						}
					}
			}
		}

		
		return found;
	}

}
