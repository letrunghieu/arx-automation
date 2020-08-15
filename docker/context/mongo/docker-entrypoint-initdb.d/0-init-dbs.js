db = (new Mongo()).getDB('anonymization');

['originalDatasets', 'outputDatasets', 'hierarchies', 'solutions', 'requests'].forEach(function(dbName) {
    db.createCollection(dbName, {});
});
