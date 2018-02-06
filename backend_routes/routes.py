from flask import Flask
from flask import jsonify
from flask import request
from flask_pymongo import PyMongo


app = Flask(__name__)

app.config['MONGO_DBNAME'] = 'db'
app.config['MONGO_URI'] = 'mongodb://localhost:27017/db'

mongo = PyMongo(app)

@app.route('/add_user', methods=['POST'])
def add_user():
  users = mongo.db.users
  user = request.headers['user']
  users.insert({'user': user})
  return jsonify({'inserted' : 'true'})


@app.route('/get_users', methods=['GET'])
def get_users():
    users = mongo.db.users
    result = convert_object(users.find().limit(100))
    return jsonify({'users' : result})

def convert_object(objects):
    result = []
    for obj in objects:
        obj['_id'] = str(obj['_id'])
        result.append(obj)
    return result


if __name__ == '__main__':
    app.run(debug=True, port = 80, host = '0.0.0.0')
