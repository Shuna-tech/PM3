package pm3.tools;

import pm3.dal.AttributesDao;
import pm3.dal.CharacterAttributesDao;
import pm3.dal.CharacterCurrenciesDao;
import pm3.dal.CharacterEquipmentsDao;
import pm3.dal.CharacterInfoDao;
import pm3.dal.CharacterJobsDao;
import pm3.dal.ConsumableAttributesDao;
import pm3.dal.ConsumablesDao;
import pm3.dal.CurrenciesDao;
import pm3.dal.EquipmentSlotsDao;
import pm3.dal.EquippableItemsDao;
import pm3.dal.GearAndWeaponAttributesDao;
import pm3.dal.GearAndWeaponJobsDao;
import pm3.dal.GearsDao;
import pm3.dal.InventoryPositionsDao;
import pm3.dal.ItemsDao;
import pm3.dal.JobsDao;
import pm3.dal.PlayersDao;
import pm3.dal.WeaponsDao;
import pm3.model.Attributes;
import pm3.model.CharacterAttributes;
import pm3.model.CharacterCurrencies;
import pm3.model.CharacterEquipments;
import pm3.model.CharacterInfo;
import pm3.model.CharacterJobs;
import pm3.model.ConsumableAttributes;
import pm3.model.Consumables;
import pm3.model.Currencies;
import pm3.model.EquippableItems;
import pm3.model.GearAndWeaponAttributes;
import pm3.model.GearAndWeaponJobs;
import pm3.model.Gears;
import pm3.model.InventoryPositions;
import pm3.model.Items;
import pm3.model.Jobs;
import pm3.model.EquipmentSlots;
import pm3.model.Players;
import pm3.model.Weapons;

import java.sql.SQLException;
import java.util.List;

public class Driver {

    public static void main(String[] args) throws SQLException {
    	testPlayerAndCharacters();
    	testCurrencies();
    	testCharacterAttributes();
    	testGearAndWeapon();
    	testJobs();
    }
    
    /**
     * Test DAO Classes:
     	* PlayersDao
     	* CharacterInfoDao
     	* CharacterEquipmentsDao
     	* EquipmentSlotsDao
     */
    private static void testPlayerAndCharacters() throws SQLException {
        PlayersDao playersDao = PlayersDao.getInstance();
        CharacterInfoDao characterInfoDao = CharacterInfoDao.getInstance();
        CharacterEquipmentsDao characterEquipmentsDao = CharacterEquipmentsDao.getInstance();
        EquipmentSlotsDao equipmentSlotsDao = EquipmentSlotsDao.getInstance();
        EquippableItemsDao equippableItemsDao = EquippableItemsDao.getInstance();
        
        try {
	        //Create a player
	        Players newPlayer = new Players("uniquePlayerTest2", "uniquePlayerTest2@example.com");
	        newPlayer = playersDao.create(newPlayer);
	        System.out.println("Inserted Player: " + newPlayer);
	
	        //Create a character
	        CharacterInfo newCharacter = new CharacterInfo("TestFirstName2", "TestLastName2", 250, newPlayer);
	        newCharacter = characterInfoDao.create(newCharacter);
	        System.out.println("Inserted Character: " + newCharacter);
	        
	        //Create a characterEquipment: insert equipment for the character using the existing slotID and itemID
	        //Use an existing slot in EquipmentSlots (e.g., "Main Hand" with slotID 5)
	        EquipmentSlots mainHandSlot = equipmentSlotsDao.getSlotById(5); // Assuming slotID 5 exists for "Main Hand"
	        if (mainHandSlot == null) {
	            System.out.println("Main Hand slot not found in EquipmentSlots.");
	            return;
	        }
	        //Use an existing item in EquippableItems (e.g., itemID 1)
	        EquippableItems existingItem = equippableItemsDao.getById(1); // Assuming itemID 1 exists in EquippableItems
	        
	        if (existingItem == null) {
	            System.out.println("Equippable item with itemID 1 not found.");
	            return;
	        }
	        CharacterEquipments newEquipment = new CharacterEquipments(newCharacter, mainHandSlot, existingItem);
	        newEquipment = characterEquipmentsDao.create(newEquipment);
	        System.out.println("Inserted Equipment: " + newEquipment);
	
	        //Get a player by user name
	        Players retrievedPlayer = playersDao.getPlayerFromUserName("uniquePlayerTest2");
	        System.out.println("Retrieved Player: " + retrievedPlayer);
	
	        //Get a character by characterID
	        CharacterInfo retrievedCharacter = characterInfoDao.getCharactersByCharacterID(newCharacter.getCharacterID());
	        System.out.println("Retrieved Character: " + retrievedCharacter);
	
	        //Get an equipment by character and slot
	        CharacterEquipments retrievedEquipment = characterEquipmentsDao.getCharacterEquipmentByCharacterAndSlot(
	            newCharacter, mainHandSlot
	        );
	        System.out.println("Retrieved Equipment: " + retrievedEquipment);
	
	        //Get all characters for the player by playerID
	        List<CharacterInfo> characterList = characterInfoDao.getCharactersByPlayerID(newPlayer.getPlayerID());
	        System.out.println("Retrieved Characters for Player ID " + newPlayer.getPlayerID() + ":");
	        for (CharacterInfo character : characterList) {
	            System.out.println(character);
	        }
	
	        //Update player's user name to a new unique value
	        playersDao.updatePlayerUserName(newPlayer, "updatedUniquePlayerTest2");
	        System.out.println("Updated Player Username: " + newPlayer);
	
	        //Update character's first name to a new unique value
	        characterInfoDao.updateCharacterFirstName(newCharacter, "UpdatedTestFirstName2");
	        System.out.println("Updated Character First Name: " + newCharacter);
	
	        //Update equipment by using another existing item in EquippableItems (e.g., itemID 2)
	        EquippableItems updatedItem = equippableItemsDao.getById(2); // Assuming itemID 2 exists in EquippableItems
	
	        if (updatedItem != null) {
	            characterEquipmentsDao.updateItem(newEquipment, updatedItem);
	            System.out.println("Updated Equipment Item: " + newEquipment);
	        } else {
	            System.out.println("Equippable item with itemID 2 not found for updating.");
	        }
	
	        //Delete equipment
	        characterEquipmentsDao.delete(newEquipment);
	        System.out.println("Deleted Equipment: " + newEquipment);
	
	        //Delete character
	        characterInfoDao.delete(newCharacter);
	        System.out.println("Deleted Character: " + newCharacter);
	
	        //Delete player
	        playersDao.delete(newPlayer);
	        System.out.println("Deleted Player: " + newPlayer);
	        }catch (SQLException e) {
	            e.printStackTrace();
	            throw e;
	        }
    }
    
    /**
     * Test DAO Classes:
     	* CurrienciesDao
     	* CharacterCurrienciesDao
     */
    private static void testCurrencies() throws SQLException {
    	// Create instances
        PlayersDao playersDao = PlayersDao.getInstance();
        CharacterInfoDao characterInfoDao = CharacterInfoDao.getInstance();
        CurrenciesDao currenciesDao = CurrenciesDao.getInstance();
        CharacterCurrenciesDao characterCurrenciesDao = CharacterCurrenciesDao.getInstance();

        try {
            // Create a test player and test character
            Players player = new Players("TestPlayer", "testplayer@example.com");
            player = playersDao.create(player);
            System.out.println("Created player with ID: " + player.getPlayerID());

            CharacterInfo character = new CharacterInfo("TestFirst", "TestLast", 100, player);
            character = characterInfoDao.create(character);
            System.out.println("Created character with ID: " + character.getCharacterID());

            System.out.println("\nTesting Currencies operations:");
            
            // Create new currency
            Currencies goldCurrency = new Currencies("Gold", 999999, 50000);
            goldCurrency = currenciesDao.create(goldCurrency);
            System.out.println("Created Gold currency with ID: " + goldCurrency.getCurrencyID());

            // Read currency by ID
            Currencies retrievedCurrency = currenciesDao.getCurrencyByID(goldCurrency.getCurrencyID());
            if (retrievedCurrency != null) {
                System.out.println("Retrieved currency: " + retrievedCurrency.getCurrencyName() + 
                                 ", Max amount: " + retrievedCurrency.getMaxAmount() +
                                 ", Weekly cap: " + retrievedCurrency.getWeeklyCap());
            }
            
            // Update the currency
            goldCurrency.setCurrencyName("Super Gold");
            goldCurrency.setMaxAmount(1000000);
            goldCurrency.setWeeklyCap(55000);
            currenciesDao.update(goldCurrency);
            System.out.println("Updated currency to: " + goldCurrency.getCurrencyName() + 
                              ", Max amount: " + goldCurrency.getMaxAmount() +
                              ", Weekly cap: " + goldCurrency.getWeeklyCap());

            System.out.println("\nTesting CharacterCurrencies operations:");
            
            // Create character currency relationship
            CharacterCurrencies characterGold = new CharacterCurrencies(character, goldCurrency, 1000, 500);
            characterGold = characterCurrenciesDao.create(characterGold);
            System.out.println("Created character currency relationship");
            
            // Update the character currency
            characterGold.setTotalAmountOwned(1500);
            characterGold.setAmountThisWeek(600);
            characterCurrenciesDao.update(characterGold);
            System.out.println("Updated character currency - Total amount: " + characterGold.getTotalAmountOwned() + 
                              ", Weekly amount: " + characterGold.getAmountThisWeek());

            // Read by character and currency IDs
            CharacterCurrencies retrievedCharacterGold = characterCurrenciesDao.getCharacterCurrencyByIDs(
                character.getCharacterID(), goldCurrency.getCurrencyID());
            if (retrievedCharacterGold != null) {
                System.out.println("Retrieved character currency - Total amount: " + 
                                 retrievedCharacterGold.getTotalAmountOwned() +
                                 ", Weekly amount: " + retrievedCharacterGold.getAmountThisWeek());
            }

            // Clean up test data
            System.out.println("\nCleaning up test data:");

            characterCurrenciesDao.delete(characterGold);
            System.out.println("Deleted character currency relationship");

            currenciesDao.delete(goldCurrency);
            System.out.println("Deleted currency");

            characterInfoDao.delete(character);
            System.out.println("Deleted character");

            playersDao.delete(player);
            System.out.println("Deleted player");

            System.out.println("\nAll currency operations completed!");
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }   
    
    /**
     * Test DAO Classes:
     	* CharacterAttributesDao
     	* InventoryPositionsDao
     */
    private static void testCharacterAttributes() throws SQLException {
    	PlayersDao playersDao = PlayersDao.getInstance();
        CharacterInfoDao characterInfoDao = CharacterInfoDao.getInstance();
        AttributesDao attributesDao = AttributesDao.getInstance();
        ItemsDao itemsDao = ItemsDao.getInstance();
        CharacterAttributesDao characterAttributesDao = CharacterAttributesDao.getInstance();
        InventoryPositionsDao inventoryPositionsDao = InventoryPositionsDao.getInstance();
        
        try {
        	Players player = playersDao.getPlayerByID(1);

            CharacterInfo character = new CharacterInfo("TestFirst", "TestLast", 100, player);
            character = characterInfoDao.create(character);

            Items item1 = new Items("Sword", 99, true, 1000);
            item1 = itemsDao.create(item1);
            
            Items item2 = new Items("Shield", 1, true, 2000);
            item2 = itemsDao.create(item2);

            Attributes attribute1 = new Attributes("Strength");
            attribute1 = attributesDao.create(attribute1);
            
            Attributes attribute2 = new Attributes("Dexterity");
            attribute2 = attributesDao.create(attribute2);

            CharacterAttributes characterAttribute1 = new CharacterAttributes(
                attribute1,
                character,
                50
            );
            characterAttribute1 = characterAttributesDao.create(characterAttribute1);

            CharacterAttributes characterAttribute2 = new CharacterAttributes(
                attribute2,
                character,
                40
            );
            characterAttribute2 = characterAttributesDao.create(characterAttribute2);

            InventoryPositions inventoryPosition1 = new InventoryPositions(
                character,
                0,
                item1,
                1
            );
            inventoryPosition1 = inventoryPositionsDao.create(inventoryPosition1);

            InventoryPositions inventoryPosition2 = new InventoryPositions(
                character,
                1,
                item2,
                1
            );
            inventoryPosition2 = inventoryPositionsDao.create(inventoryPosition2);

            System.out.println("Created character: " + character.getFirstName() + " " + character.getLastName());
            
            List<CharacterAttributes> attributes = characterAttributesDao.getCharacterAttributes(character.getCharacterID());
            for(CharacterAttributes ca : attributes) {
                System.out.println("Attribute: " + ca.getAttributes().getAttributeName() + " = " + ca.getAttributeValue());
            }
            
            List<InventoryPositions> inventory = inventoryPositionsDao.getInventoryPositions(item1.getItemID());
            for(InventoryPositions ip : inventory) {
                System.out.println("Inventory: " + ip.getItems().getItemName() + " at position " + ip.getStackPosition());
            }

            attributesDao.delete(attribute1);
            attributesDao.delete(attribute2);
            itemsDao.delete(item1.getItemID());
            itemsDao.delete(item2.getItemID());
            characterInfoDao.delete(character);
        }catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Test DAO Classes:
     	* ItemsDao
     	* EquippableItemsDao
     	* ConsumablesDao
     	* GearsDao
     	* WeaponsDao
     	* ConsumableAttributesDao
     	* GearAndWeaponAttributesDao
     	* AttributesDao
     */
    private static void testGearAndWeapon() throws SQLException {
	   	 System.out.println("--------------------------------------------------------------------");
	     System.out.println("TABLE Items: create, getItemById, delete");
	     System.out.println("TABLE Attributes: create, getAttributeByAttributeId");
	     System.out.println("TABLE ConsumableAttributes: create, getConsumableAttributeByItemIdAndAttributeId, getAttributesByItemId, updateConsumableAttribute");
	     System.out.println("TABLE Consumables: create, getConsumableByItemID, updateConsumable, updateDescription, getConsumablesByItemLevel, delete");
	     System.out.println("TABLE EquippablesItems: create");
	     System.out.println("TABLE GearAndWeaponAttributes: create, getAttributeByIds");
	     System.out.println("TABLE GearAndWeaponJobs: create, getItemByID");
	     System.out.println("TABLE Gears: create, getGearByPartialNAme, delete");
	 	 System.out.println("TABLE Weapons: create, getWeaponByItemID, getAllWeapons, update, delete");
	 	 System.out.println("--------------------------------------------------------------------");
	 	 
	 	try {
            // Initialize DAOs once
	 		ItemsDao itemsDao = ItemsDao.getInstance();
            AttributesDao attributesDao = AttributesDao.getInstance();
            ConsumableAttributesDao consumableAttributesDao = ConsumableAttributesDao.getInstance();
            ConsumablesDao consumablesDao = ConsumablesDao.getInstance();
            EquipmentSlotsDao slotsDao = EquipmentSlotsDao.getInstance();
            EquippableItemsDao equippableItemsDao = EquippableItemsDao.getInstance();
            GearAndWeaponAttributesDao gearAndWeaponAttributesDao = GearAndWeaponAttributesDao.getInstance();
            GearAndWeaponJobsDao gearAndWeaponJobsDao = GearAndWeaponJobsDao.getInstance();
            GearsDao gearsDao = GearsDao.getInstance();
            WeaponsDao weaponsDao = WeaponsDao.getInstance();
            
            
            System.out.println("=== CREATING RECORDS ===");
            // Create Items
            Items potion = new Items("Health Potion", 99, true, 100);
            Items savedPotion = itemsDao.create(potion);
            System.out.println("Created item: Health Potion");

            Items sword = new Items("Iron Sword", 1, true, 500);
            Items savedSword = itemsDao.create(sword);
            System.out.println("Created item: Iron Sword");

            Items armor = new Items("Iron Armor", 1, true, 450);
            Items savedArmor = itemsDao.create(armor);
            System.out.println("Created item: Iron Armor");

            // Create Attributes
            Attributes strength = new Attributes("Strength");
            Attributes savedStrength = attributesDao.create(strength);
            System.out.println("Created attribute: Strength");

            // Create ConsumableAttributes
            ConsumableAttributes potionEffect = new ConsumableAttributes(
                savedPotion, savedStrength, 100, 0.25f
            );
            ConsumableAttributes savedPotionEffect = consumableAttributesDao.create(potionEffect);
            System.out.println("Created consumable attribute for potion");

            // Create Consumables
            Consumables healthPotion = new Consumables(savedPotion.getItemID(), 1, "Restores 100 HP");
            Consumables savedHealthPotion = consumablesDao.create(healthPotion);
            System.out.println("Created consumable: Health Potion");
            
            // Create Two Slots
            EquipmentSlots mainHandSlot = new EquipmentSlots("Main Hand");
            EquipmentSlots armorSlot = new EquipmentSlots("Armor");

    
            EquipmentSlots savedMainHandSlot = slotsDao.create(mainHandSlot);
            EquipmentSlots savedArmorSlot = slotsDao.create(armorSlot);

            System.out.println("Created equipment slots");
     
            System.out.println("Main hand slot: " + (savedMainHandSlot != null ? savedMainHandSlot.getSlotID() : "null"));
            System.out.println("Armor slot: " + (savedArmorSlot != null ? savedArmorSlot.getSlotID() : "null"));

            
            EquippableItems equippableSword = new EquippableItems(
                    savedSword.getItemID(), 
                    savedSword.getItemName(),  
                    savedSword.getMaxStackSize(),
                    savedSword.isMarketAllowed(), 
                    savedSword.getVendorPrice(), 
                    1,                 
                    savedMainHandSlot,      
                    1                  
                );
            
            EquippableItems savedEquippableSword = equippableItemsDao.create(equippableSword);
            System.out.println("Created equippable item: Sword");
            
            EquippableItems equippableArmor = new EquippableItems(
                    savedArmor.getItemID(),  
                    savedArmor.getItemName(),
                    savedArmor.getMaxStackSize(), 
                    savedArmor.isMarketAllowed(),
                    savedArmor.getVendorPrice(),
                    2,               
                    savedArmorSlot,     
                    1            
                );

            EquippableItems savedEquippableArmor = equippableItemsDao.create(equippableArmor);
            
            System.out.println("Created equippable item: Armor");

            // Create GearAndWeaponAttributes
            GearAndWeaponAttributes swordStrength = new GearAndWeaponAttributes(
           		 savedEquippableSword, savedStrength, 5
            );
            GearAndWeaponAttributes savedSwordStrength = gearAndWeaponAttributesDao.create(swordStrength);
            System.out.println("Created gear attribute for sword");
            
            
            // create job
            JobsDao jobsDao = JobsDao.getInstance();
            Jobs warriorJob = new Jobs("Warrior");
            Jobs savedWarriorJob = jobsDao.create(warriorJob);
            
            // Create GearAndWeaponJobs
            GearAndWeaponJobs swordJob = new GearAndWeaponJobs(savedEquippableSword, savedWarriorJob);
            GearAndWeaponJobs savedSwordJob = gearAndWeaponJobsDao.create(swordJob);
            System.out.println("Created gear job for sword");
            

            // Create Weapon
            Weapons ironSword = new Weapons(
                    savedEquippableSword.getItemID(), 
                    savedEquippableSword.getItemName(), 
                    savedEquippableSword.getMaxStackSize(),
                    savedEquippableSword.isMarketAllowed(),
                    savedEquippableSword.getVendorPrice(), 
                    10,              
                    mainHandSlot,    
                    5,              
                    150,            
                    2.5,            // autoAttack
                    2.8             // attackDelay
                );
      
            Weapons savedIronSword = weaponsDao.create(ironSword);
            System.out.println("Created weapon: Iron Sword");


            // Create Weapons
            Gears ironArmor = new Gears(
                    savedArmor.getItemID(),
                    savedArmor.getItemName(),
                    savedArmor.getMaxStackSize(),
                    savedArmor.isMarketAllowed(),
                    savedArmor.getVendorPrice(),
                    10,          
                    armorSlot,       
                    5,         
                    100,           
                    50             
                );
            Gears savedIronArmor = gearsDao.create(ironArmor);
            System.out.println("Created gear: Iron Armor");
     

            
            System.out.println("\n=== READING RECORDS ===");
            // Read Items
            Items retrievedItem = itemsDao.getItemById(savedPotion.getItemID());
            System.out.println("Retrieved item: " + retrievedItem.getItemName());

            // Read Attributes
            Attributes retrievedAttribute = attributesDao.getAttributeByAttributesID(savedStrength.getAttributeID());
            System.out.println("Retrieved attribute: " + retrievedAttribute.getAttributeName());

            // Read ConsumableAttributes
            ConsumableAttributes retrievedEffect = consumableAttributesDao
                .getConsumableAttributeByItemIdAndAttributeId(savedPotion.getItemID(), savedStrength.getAttributeID());
            System.out.println("Retrieved consumable attribute");

            List<ConsumableAttributes> attributesList = consumableAttributesDao.getAttributesByItemId(savedPotion.getItemID());
            System.out.println("Retrieved consumable attributes list, count: " + attributesList.size());

            // Read Consumables
            Consumables retrievedConsumable = consumablesDao.getConsumableByItemId(savedPotion.getItemID());
            System.out.println("Retrieved consumable: " + retrievedConsumable.getDescription());

            List<Consumables> levelConsumables = consumablesDao.getConsumablesByItemLevel(1);
            System.out.println("Retrieved consumables by level, count: " + levelConsumables.size());
            
            // Read GearAndWeaponAttributes
            GearAndWeaponAttributes retrievedGearAndWeaponAttributes = gearAndWeaponAttributesDao.getGearAttributeByIds(savedSword.getItemID(), savedStrength.getAttributeID());
            System.out.println("Retrieved gear and weapon attributes");

            // Read GearAndWeaponJobs
            GearAndWeaponJobs retrievedJob = gearAndWeaponJobsDao.getByItemId(savedSword.getItemID());
            System.out.println("Retrieved gear and weapon job");

            // Read Gears
            List<Gears> armorList = gearsDao.getGearByPartialName("Iron");
            System.out.println("Retrieved gear by partial name, count: " + armorList.size());

            // Read Weapons
            Weapons retrievedWeapon = weaponsDao.getWeaponByItemID(savedSword.getItemID());
            System.out.println("Retrieved weapon by ID");

            List<Weapons> allWeapons = weaponsDao.getAllWeapons();
            System.out.println("Retrieved all weapons, count: " + allWeapons.size());

            System.out.println("\n=== UPDATING RECORDS ===");
            // Update ConsumableAttributes
            savedPotionEffect.setAttributeBonusCap(150);
            consumableAttributesDao.updateConsumableAttribute(savedPotionEffect);
            System.out.println("Updated consumable attribute");

            // Update Consumables
            savedHealthPotion.setItemLevel(2);
            consumablesDao.updateConsumable(savedHealthPotion);
            System.out.println("Updated consumable level");

            consumablesDao.updateDescription(savedHealthPotion, "Restores 150 HP");
            System.out.println("Updated consumable description");

            // Update Weapons
            savedIronSword.setPhysicalDamage(20);
            weaponsDao.update(savedIronSword);
            System.out.println("Updated weapon damage");

            System.out.println("\n=== DELETING RECORDS ===");
            // Delete Weapons
            weaponsDao.delete(savedIronSword.getItemID());
            System.out.println("Deleted weapon");

            // Delete Gears
            gearsDao.delete(savedIronArmor);
            System.out.println("Deleted gear");

            // Delete Consumables
            consumablesDao.delete(savedHealthPotion);
            System.out.println("Deleted consumable");

            // Delete Items (should cascade)
            itemsDao.delete(savedPotion.getItemID());
            itemsDao.delete(savedSword.getItemID());
            itemsDao.delete(savedArmor.getItemID());
            System.out.println("Deleted items");

            System.out.println("\nAll operations completed successfully!");
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }
    
    /**
     * Test DAO Classes:
     	* JobsDao
     	* CharacterJobsDao
     */
    private static void testJobs() throws SQLException {
    	// Create DAOs
        JobsDao jobsDao = JobsDao.getInstance();
        CharacterJobsDao characterJobsDao = CharacterJobsDao.getInstance();
        CharacterInfoDao characterInfoDao = CharacterInfoDao.getInstance();

        try {
            System.out.println("\nTesting Jobs operations:");
            
            // Create new jobs
            Jobs warrior = new Jobs("Warrior");
            warrior = jobsDao.create(warrior);
            System.out.println("Created Warrior job with ID: " + warrior.getJobID());

            Jobs mage = new Jobs("Mage");
            mage = jobsDao.create(mage);
            System.out.println("Created Mage job with ID: " + mage.getJobID());

            Jobs rogue = new Jobs("Rogue");
            rogue = jobsDao.create(rogue);
            System.out.println("Created Rogue job with ID: " + rogue.getJobID());

            // Update job name
            warrior.setJobName("Elite Warrior");
            Jobs updatedJob = jobsDao.updateJobName(warrior, warrior.getJobName());
            System.out.println("Updated job name to: " + updatedJob.getJobName());

            // Read all jobs
            System.out.println("\nRetrieving all jobs:");
            for (Jobs job : jobsDao.getAllJobs()) {
                System.out.println("Job: " + job.getJobName() + 
                                 " (ID: " + job.getJobID() + ")");
            }

            System.out.println("\nTesting CharacterJobs operations:");
            
            // Get existing characters for testing
            CharacterInfo character1 = characterInfoDao.getCharactersByCharacterID(1);
            CharacterInfo character2 = characterInfoDao.getCharactersByCharacterID(2);

            // Create character-job relationships
            CharacterJobs char1Warrior = new CharacterJobs(character1, warrior, 1, 0, true);
            char1Warrior = characterJobsDao.create(char1Warrior);
            System.out.println("Created character-job relationship: Character " + 
                             char1Warrior.getCharacter().getCharacterID() + 
                             " as " + char1Warrior.getJob().getJobName());

            CharacterJobs char1Mage = new CharacterJobs(character1, mage, 1, 0, false);
            char1Mage = characterJobsDao.create(char1Mage);
            System.out.println("Created character-job relationship: Character " + 
                             char1Mage.getCharacter().getCharacterID() + 
                             " as " + char1Mage.getJob().getJobName());

            CharacterJobs char2Rogue = new CharacterJobs(character2, rogue, 1, 0, true);
            char2Rogue = characterJobsDao.create(char2Rogue);
            System.out.println("Created character-job relationship: Character " + 
                             char2Rogue.getCharacter().getCharacterID() + 
                             " as " + char2Rogue.getJob().getJobName());

            // Update character job
            char1Warrior = characterJobsDao.updateLevelAndExperience(char1Warrior, 5, 1000);
            System.out.println("\nUpdated character job - Level: " + char1Warrior.getLevel() + 
                             ", Experience: " + char1Warrior.getExperiencePoints());

            // Read character jobs
            System.out.println("\nRetrieving jobs for character 1:");
            for (CharacterJobs job : characterJobsDao.getCharacterJobsByCharacter(character1)) {
                System.out.println("Job: " + job.getJob().getJobName() + 
                                 ", Level: " + job.getLevel() + 
                                 ", Current: " + job.isCurrentJob());
            }

            // Clean up test data
            System.out.println("\nCleaning up test data:");

            characterJobsDao.delete(char2Rogue);
            System.out.println("Deleted character-job relationship for Character " + 
                             char2Rogue.getCharacter().getCharacterID());

            jobsDao.delete(mage);
            System.out.println("Deleted job: " + mage.getJobName());

            System.out.println("\nAll job operations completed!");

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
