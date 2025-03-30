//La base de datos debe llamarse comercio. 'use comercio'
db.Usuario.insertMany([
  {
    "user_Id": 1,
    "name": "José López",
    "email": "joselopez@example.com",
    "age": 28,
    "direction": "Calle Príncipe, 36202 Vigo, Pontevedra, España"
  },
  {
    "user_Id": 2,
    "name": "Anna González",
    "email": "annagonzalez@example.com",
    "age": 34,
    "direction": "Avenida de Madrid, 36204 Vigo, Pontevedra, España"
  },
  {
    "user_Id": 3,
    "name": "Carlos Pérez",
    "email": "carlosperez@example.com",
    "age": 42,
    "direction": "Rúa de García Barbón, 36201 Vigo, Pontevedra, España"
  },
  {
    "user_Id": 4,
    "name": "Laura Martínez",
    "email": "lauramartinez@example.com",
    "age": 29,
    "direction": "Calle Rosalía de Castro, 36203 Vigo, Pontevedra, España"

  },
  {
    "user_Id": 5,
    "name": "Miguel Rodríguez",
    "email": "miguelrodriguez@example.com",
    "age": 37,
    "direction": "Rúa de Colón, 36201 Vigo, Pontevedra, España"
  },
  {
    "user_Id": 6,
    "name": "Marta Fernández",
    "email": "martafernandez@example.com",
    "age": 31,
    "direction": "Calle de Urzáiz, 36203 Vigo, Pontevedra, España"
  }
]);

db.Carrito.insertMany([
  {
    "user_Id": 1,
    "items": [
      {
        "game_Id": 1,
        "title": "The Legend of Zelda: Breath of the Wild",
        "quantity": 1,
        "price": 59.99
      },
      {
        "game_Id": 5,
        "title": "Hollow Knight",
        "quantity": 1,
        "price": 14.99
      }
    ]
  },
  {
    "user_Id": 2,
    "items": [
      {
        "game_Id": 2,
        "title": "God of War Ragnarok",
        "quantity": 1,
        "price": 69.99
      }
    ]
  },
  {
    "user_Id": 3,
    "items": [
      {
        "game_Id": 3,
        "title": "Cyberpunk 2077",
        "quantity": 1,
        "price": 49.99
      },
      {
        "game_Id": 4,
        "title": "EA Sports FC 25",
        "quantity": 1,
        "price": 69.99
      }
    ]
  },
  {
    "user_Id": 4,
    "items": [
      {
        "game_Id": 4,
        "title": "EA Sports FC 25",
        "quantity": 1,
        "price": 69.99
      }
    ]
  },
  {
    "user_Id": 5,
    "items": [
      {
        "game_Id": 5,
        "title": "Hollow Knight",
        "quantity": 1,
        "price": 14.99
      }
    ]
  },
  {
    "user_Id": 6,
    "items": [
      {
        "game_Id": 6,
        "title": "Spider-Man 2",
        "quantity": 1,
        "price": 79.99
      },
      {
        "game_Id": 10,
        "title": "Resident Evil 4 Remake",
        "quantity": 1,
        "price": 59.99
      }
    ]
  }
]);

db.Compras.insertMany([
  {
    "purchase_id": 1,
    "user_Id": 1,
    "items": [
      {
        "type": "videogame",
        "game_id": 4,
        "name": "EA Sports FC 25",
        "quantity": 1,
        "unit_price": 69.99
      },
      {
        "type": "videogame",
        "game_id": 8,
        "name": "Elden Ring",
        "quantity": 1,
        "unit_price": 59.99
      }
    ],
    "total": 129.98,
    "purchase_date": ISODate()
  },
  {
    "purchase_id": 2,
    "user_Id": 2,
    "items": [
      {
        "type": "videogame",
        "game_id": 2,
        "name": "God of War Ragnarok",
        "quantity": 1,
        "unit_price": 69.99
      },
      {
        "type": "videogame",
        "game_id": 3,
        "name": "Cyberpunk 2077",
        "quantity": 1,
        "unit_price": 49.99
      }
    ],
    "total": 119.98,
    "purchase_date": ISODate()
  },
  {
    "purchase_id": 3,
    "user_Id": 3,
    "items": [
      {
        "type": "videogame",
        "game_id": 4,
        "name": "EA Sports FC 25",
        "quantity": 2,
        "unit_price": 69.99
      }
    ],
    "total": 139.98,
    "purchase_date": ISODate()
  },
  {
    "purchase_id": 4,
    "user_Id": 4,
    "items": [
      {
        "type": "videogame",
        "game_id": 5,
        "name": "Hollow Knight",
        "quantity": 1,
        "unit_price": 14.99
      },
      {
        "type": "videogame",
        "game_id": 7,
        "name": "Final Fantasy XVI",
        "quantity": 1,
        "unit_price": 59.99
      }
    ],
    "total": 74.98,
    "purchase_date": ISODate()
  },
  {
    "purchase_id": 5,
    "user_Id": 5,
    "items": [
      {
        "type": "videogame",
        "game_id": 6,
        "name": "Spider-Man 2",
        "quantity": 1,
        "unit_price": 79.99
      },
      {
        "type": "videogame",
        "game_id": 10,
        "name": "Resident Evil 4 Remake",
        "quantity": 1,
        "unit_price": 59.99
      }
    ],
    "total": 139.98,
    "purchase_date": ISODate()
  },
  {
    "purchase_id": 6,
    "user_Id": 6,
    "items": [
      {
        "type": "videogame",
        "game_id": 8,
        "name": "Elden Ring",
        "quantity": 1,
        "unit_price": 59.99
      },
      {
        "type": "videogame",
        "game_id": 9,
        "name": "The Witcher 3: Wild Hunt",
        "quantity": 1,
        "unit_price": 49.99
      }
    ],
    "total": 109.98,
    "purchase_date": ISODate()
  },
  {
    "purchase_id": 7,
    "user_Id": 1,
    "items": [
      {
        "type": "videogame",
        "game_id": 5,
        "name": "Hollow Knight",
        "quantity": 1,
        "unit_price": 14.99
      },
      {
        "type": "videogame",
        "game_id": 7,
        "name": "Final Fantasy XVI",
        "quantity": 1,
        "unit_price": 59.99
      }
    ],
    "total": 74.98,
    "purchase_date": ISODate()
  }
]);
