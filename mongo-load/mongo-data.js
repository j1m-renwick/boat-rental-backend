db = db.getSiblingDB('junk')
db.trips.drop()
db.createCollection("trips")
db.trips.save({ "name" : "Jim's Summer Getaway", "tickets" : 12345, "junkName" : "Mary Sue II", "departureDttm" : ISODate("2019-08-25T11:00:00.000Z"), "harbour" : "VICTORIA_HARBOUR", "type" : "BOOZE_CRUISE", "description" : "Gonna get loaded and tear it up boiiiii!"})

db.trips.save({ "name" : "Bob's Away Day '19", "tickets" : 321, "junkName" : "The Olivia Dream", "departureDttm" : ISODate("2019-08-25T13:00:00.000Z"), "harbour" : "VICTORIA_HARBOUR", "type" : "CASINO_ROYALE", "description" : "Getting away from the family for a while :)"})

db.trips.save({ "name" : "Another day in paradise", "tickets" : 3121, "junkName" : "The Rowboat", "departureDttm" : ISODate("2019-09-01T10:00:00.000Z"), "harbour" : "PORT_SHELTER", "type" : "BOOZE_CRUISE", "description" : "Chasing the sunburn..."})

db.trips.save({ "name" : "Cruising the day away", "tickets" : 2, "junkName" : "Troubled Waters III", "departureDttm" : ISODate("2019-08-25T09:00:00.000Z"), "harbour" : "PORT_SHELTER", "type" : "MAX_RELAX", "description" : "Fish Ahoy!!!"})

db.trips.save({ "name" : "Nick's Pirate Cruise lol", "tickets" : 990, "junkName" : "The Salty Shanty", "departureDttm" : ISODate("2019-09-01T13:00:00.000Z"), "harbour" : "TAI_TAM_HARBOUR", "type" : "CASINO_ROYALE", "description" : "Yarrr. That is all. "})

