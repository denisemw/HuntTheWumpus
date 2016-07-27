package View;

public enum Views {
	/**
	 * Shows the previously shown view
	 */
	PREVIOUS,
	/**
	 * Shows a new GameView panel
	 */
	GAME,
	/**
	 * Shows a new MiniMap panel
	 */
	MINIMAP,
	/**
	 * Shows a new NewGameSelection panel
	 */
	NEWGAME,
	/**
	 * Shows a new HostGameSelection panel
	 */
	HOSTGAME,
	/**
	 * Shows a new JoinGameSelection panel
	 */
	JOINGAME,
	/**
	 * Shows a new ContinueGameSelection panel
	 */
	CONTINUEGAME,
	/**
	 * Shows a new TitleView panel
	 */
	TITLE,
	/**
	 * Shows a new PlayAnimation panel with the falling player gif
	 */
	FALLINGPLAYER,
	/**
	 * Shows a new PlayAnimation panel with the player being eaten by a wumpus
	 * gif
	 */
	HUNGRYWUMPUS,
	/**
	 * Shows a new CutsceneView which then determines the beginning cutscene to
	 * play
	 */
	BEGINNING_CUTSCENE,
	/**
	 * Shows a new PlayAnimation panel with the classic dead wumpus gif
	 */
	DEADWUMPUS_CLASSICAL,
	/**
	 * Shows a new PlayAnimation panel with the western dead wumpus gif
	 */
	DEADWUMPUS_WESTERN,
	/**
	 * Shows a new PlayAnimation panel with the space dead wumpus gif
	 */
	DEADWUMPUS_SPACE,
	/**
	 * Shows a new PlayAnimation panel with the player got killed by another
	 * player gif
	 */
	PLAYERGOTKILLED,
	/**
	 * Shows a new PlayAnimation panel with the player died from lack of energy
	 * gif
	 */
	STARVEDPLAYER,
	/**
	 * Shows a new PlayAnimation panel with an arrow shooting gif
	 */
	ARROWSHOOTING,
	/**
	 * Shows a new PlayAnimation panel with a laser beam being shot gif
	 */
	LASERBEAM,
	/**
	 * Shows a new PlayAnimation panel with a bullet shooting gif
	 */
	BULLETSHOOTING,
	/**
	 * Shows a new PlayAnimation with the bat taking the player gif
	 */
	BATTAKINGPLAYER;
}
