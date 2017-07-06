package jdraranor;

import java.util.HashMap;
import java.util.function.Consumer;

import jdraranor.UserInstance;
import jdraranor.jdrchannel.AccueilChannel;
import jdraranor.jdrchannel.FloodChannel;
import jdraranor.jdrchannel.JDRChannel;
import jdraranor.jdrchannel.PersoChannel;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

public class JDRAranorEngine extends ListenerAdapter {
	
	private final static String INVITE = "https://discord.gg/ademG88";
	private final static String ID = "271266877961928704";
	private final static String SANS_PERSO = "275635891593019394";
	private static Guild aranor;
	private static TextChannel accueil, flood;
	private static GuildController controller;
	
	private static HashMap<User, UserInstance> instances;
	private static HashMap<TextChannel, JDRChannel> channelManagers;
	
	private static JDRAranorEngine instance;
	
	private JDRAranorEngine(JDA jda){
		instances = new HashMap<User, UserInstance>();
		aranor= jda.getGuildById(ID);
		controller = aranor.getController();
		accueil = aranor.getTextChannelById("273613977446121472");
		flood = aranor.getTextChannelById(ID);
		channelManagers = new HashMap<TextChannel, JDRChannel>();
		TextChannel flood = aranor.getTextChannelById(ID);
		channelManagers.put(accueil, AccueilChannel.build(accueil));
		channelManagers.put(flood, FloodChannel.build(flood));
		
		initFiles();
	}
	
	private void initFiles(){
		JDRFileManager.init();
		
		for(TextChannel t : aranor.getTextChannels()){
			if( !t.equals(flood) && ! t.equals(accueil))
				t.delete().queue();
		}
		;
		for(Member m : aranor.getMembers())
			controller.addRolesToMember(m, aranor.getRoleById(SANS_PERSO)).queue();
	}
	
	public static JDRAranorEngine getInstance(JDA jda){
		if(instance == null)
			instance = new JDRAranorEngine(jda);
		return instance;
	}
	
	public static UserInstance geUserInstance(User user){
		return JDRAranorEngine.getInstance(null).getUserInstance(user);
	}
	
	public UserInstance getUserInstance(User user){
		if(!instances.containsKey(user))
			instances.put(user, new UserInstance());
		return instances.get(user);
	}
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event){
		controller.addRolesToMember(event.getMember(), aranor.getRoleById(SANS_PERSO)).queue();
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		//Ceci ne nous concerne pas		
		if(!event.getGuild().equals(aranor))
			return;
		
		TextChannel channel = event.getChannel();
		Message message = event.getMessage();
		UserInstance instance = JDRAranorEngine.geUserInstance(event.getMember().getUser());
		if(!channelManagers.containsKey(channel)){
			System.out.println("Ne possède pas channel: " + channel.getId());
			return;
		}
		if(!event.getMember().getUser().isBot()){
			if(instance.ask != null)
				channelManagers.get(channel).asking(message, instance);
			else
				channelManagers.get(channel).answer(message, instance);
		}
		Log(event.getAuthor(), event.getMessage(), event.getChannel());
	}
	
	public static void launchGame(UserInstance instance, User user){
		controller.createTextChannel(instance.perso.getName().replaceAll(" ", "_")).queue(new Consumer<TextChannel>() {
			@Override
			public void accept(TextChannel channel) {
				channelManagers.put(channel, PersoChannel.build(channel, instance).setLastChannel(accueil));
				channel.createPermissionOverride(aranor.getRoleById(ID)).queue(new Consumer<PermissionOverride>() {

					@Override
					public void accept(PermissionOverride permission) {
						permission.getManager().deny(Permission.MESSAGE_WRITE).queue();;
					}
				});
				channel.createPermissionOverride(aranor.getMember(user)).queue(new Consumer<PermissionOverride>() {

					@Override
					public void accept(PermissionOverride permission) {
						permission.getManager().grant(Permission.MESSAGE_WRITE).queue();
					}
				});
				controller.removeRolesFromMember(aranor.getMember(user), aranor.getRoleById(SANS_PERSO)).queue();
			}
		});
	}
	
	public static void goBackTo(TextChannel channel, TextChannel lastChannel, User user){
		if(lastChannel == accueil){
			System.out.println("bob");
			channelManagers.remove(channel);
			System.out.println("bob1");
			channel.delete().queue();
			System.out.println("bob2");
			controller.addRolesToMember(aranor.getMember(user), aranor.getRoleById(SANS_PERSO)).queue();
		}
	}
	
	public static String getInvite(){
		return "Ce n'est pas encore prêt :P";
		//return INVITE;
	}
	private void Log(User author, Message msg, TextChannel channel){
		System.out.printf("[%s][%s] %s: %s\n", "ARANOR",
				channel.getName(),author.getName(), msg.getContent());		
	}
	
	public String quitterSession(User author, TextChannel channel){
		if(channel.equals(accueil))
			return "Impossible de quitter l'accueil";
		else
			return "Fin de session, retour �? l'accueil";
	}
	

}
