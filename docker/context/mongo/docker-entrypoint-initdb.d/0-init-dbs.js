db = (new Mongo()).getDB('anonymization');

['originalDatasets', 'outputDatasets', 'solutions', 'requests'].forEach(function(dbName) {
    db.createCollection(dbName, {});
});