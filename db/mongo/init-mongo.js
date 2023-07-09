db.createUser({
    user: 'root',
    pwd: 'toor',
    roles: [
        {
            role: 'readWrite',
            db: 'testDB',
        },
    ],
});
db.createCollection('app_users', { capped: false });

db.app_users.insert([
    {
        "username": "ragnar777",
        "dni": "VIKI771012HMCRG093",
        "enabled": true,
        "password": "$2a$10$d4VY8HPQbp0Uecj6IG.ade2td46A2yLj5SmGefVzzdqSwCO6Ep1CG",
        "role":
            {
                "granted_authorities": ["read"]
            }
    },
    {
        "username": "heisenberg",
        "dni": "BBMB771012HMCRR022",
        "enabled": true,
        "password": "$2a$10$qNNRwI3MXAjJ5UHgZt3mMuhJn0zN/kLeGg/jDtQWwH7nyyQJwkbyq",
        "role":
            {
                "granted_authorities": ["read"]
            }
    },
    {
        "username": "misterX",
        "dni": "GOTW771012HMRGR087",
        "enabled": true,
        "password": "$2a$10$AUYuRCXOU3D/x7NThdDAHu39XeMcRH1uNtCED7xixrTItRFhkXG7C",
        "role":
            {
                "granted_authorities": ["read", "write"]
            }
    },
    {
        "username": "neverMore",
        "dni": "WALA771012HCRGR054",
        "enabled": true,
        "password": "$2a$10$uCc8mnfjRPWTwoBgk3aZJuTvDg5Ng2gXFOgJuX.wzh9TaSG7yrkCS",
        "role":
            {
                "granted_authorities": ["write"]
            }
    }
]);