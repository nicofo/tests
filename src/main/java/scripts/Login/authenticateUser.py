from locust import HttpLocust, TaskSet, task
import json

class MyTaskSet(TaskSet):
  @task
  def my_task(self):
    payload = { "userVal": "script@correu.es", "password": "Scripts1234"}
    headers = {'content-type': 'application/json'}
    r = self.client.post("/authenticateUser",json.dumps(payload), headers=headers, catch_response=True)
    print (r.text)


class MyLocust(HttpLocust):
  host = "https://crumbit-1304.appspot.com/_ah/api/loginAPI/v1"
  min_wait = 1000
  max_wait = 1000
  task_set = MyTaskSet

 
