from locust import HttpLocust, TaskSet, task
import json

g_c = 0;

class MyTaskSet(TaskSet):
  @task
  def my_task(self):
    payload = {"mail": "script" + str(g_c)+ "@correu.es","userName": "Scripts"+ str(g_c),"password": "Scripts1234"}
    headers = {'content-type': 'application/json'}
    r = self.client.post("/createUser",json.dumps(payload), headers=headers, catch_response=True)
    print r.text


class MyLocust(HttpLocust):
  host = "https://crumbit-1304.appspot.com/_ah/api/loginAPI/v1"
  min_wait = 1000
  max_wait = 1000
  task_set = MyTaskSet