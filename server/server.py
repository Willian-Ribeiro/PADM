#!/usr/bin/python

import cherrypy
import sqlite3
import json
import os
import mimetypes
import traceback

"""
Simple video streaming server

@author Diogo S. Martins <santana.martins@ufabc.edu.br>
"""

VIDEO_DB = "data/videos.db"
ALL_VIDEOS = "SELECT * FROM videos"
GET_VIDEO = "SELECT file FROM videos WHERE id=?"
GET_THUMBNAIL = "SELECT thumbnail FROM videos WHERE id=?"
INCREMENT_VIEWS = "UPDATE videos SET views=? WHERE id=?"
INCREMENT_LIKES = "UPDATE videos SET likes=? WHERE id=?"
INCREMENT_DISLIKES = "UPDATE videos SET dislikes=? WHERE id=?"
SELECT_VIEWS = "SELECT views from videos WHERE id=?"
SELECT_LIKES = "SELECT likes from videos WHERE id=?"
SELECT_DISLIKES = "SELECT dislikes from videos WHERE id=?"
VIDEO_DIR = "data/media"

BUFF_SIZE = 1024 * 5


class VideoServer:

    def __init__(self):
        pass

    @cherrypy.expose
    def index(self):
        return "Video server"

    @cherrypy.expose
    def videos(self):
        try:
            self.conn = sqlite3.connect(VIDEO_DB)
            self.cursor = self.conn.cursor()
            videos_obj = {
                "videos": []
            }
            for row in self.cursor.execute(ALL_VIDEOS):
                videos_obj['videos'].append({
                    "id": row[0],
                    "title": row[1],
                    "event": row[2],
                    "duration": row[3],
                    "recorded": row[4],
                    "speaker": row[5],
                    "views": row[6],
                    "likes": row[7],
                    "dislikes": row[8],
                    "source": row[9]
                })
            self.conn.close()
        except sqlite3.Error as e:
            traceback.print_exc(e)
            cherrypy.response.status = 400
            return json.dumps({
                "error": "failed to query the database"
            })

        return json.dumps(videos_obj)

    @cherrypy.expose
    def video(self, id=None):
        if not id:
            cherrypy.response.status = 400
            return json.dumps({
                'error': 'No video specified'
            })
        self.conn = sqlite3.connect(VIDEO_DB)
        self.cursor = self.conn.cursor()
        self.cursor.execute(GET_VIDEO, (id,))
        result = self.cursor.fetchone()
        if result is None or len(result) == 0:
            cherrypy.response.status = 400
            return json.dumps({
                'error': 'video id not found'
            })
        video_path = os.path.join(VIDEO_DIR, result[0])
        video_file = open(video_path, 'rb')
        video_size = os.path.getsize(video_path)
        mime_type = mimetypes.guess_type(video_path)[0]

        cherrypy.response.headers["Content-Type"] = mime_type
        cherrypy.response.headers["Content-Disposition"] = (
            'attachment; filename="%s"' % os.path.basename(video_path)
        )
        cherrypy.response.headers["Content-Length"] = video_size

        def stream():
            data = video_file.read(BUFF_SIZE)
            while len(data) > 0:
                yield data
                data = video_file.read(BUFF_SIZE)

        return stream()

    @cherrypy.expose
    def thumbnail(self, id):
        if not id:
            return json.dumps({
                'error': 'No thumbnail specified'
            })
        self.conn = sqlite3.connect(VIDEO_DB)
        self.cursor = self.conn.cursor()
        self.cursor.execute(GET_THUMBNAIL, (id,))
        result = self.cursor.fetchone()
        if result is None or len(result) == 0:
            cherrypy.response.status = 400
            return json.dumps({
                'error': 'thumbnail id not found'
            })
        image_path = os.path.join(VIDEO_DIR, result[0])
        image_file = open(image_path, 'rb')
        image_size = os.path.getsize(image_path)
        mime_type = mimetypes.guess_type(image_path)[0]

        cherrypy.response.headers["Content-Type"] = mime_type
        cherrypy.response.headers["Content-Disposition"] = (
            'attachment; filename="%s"' % os.path.basename(image_path)
        )
        cherrypy.response.headers["Content-Length"] = image_size

        def stream():
            data = image_file.read(BUFF_SIZE)
            while len(data) > 0:
                yield data
                data = image_file.read(BUFF_SIZE)

        return stream()

    @cherrypy.expose
    def view(self, id=None):
        if not id:
            cherrypy.response.status = 400
            return json.dumps({
                'error': 'No id has been specified'
            })
        try:
            self.conn = sqlite3.connect(VIDEO_DB)
            self.cursor = self.conn.cursor()
            self.cursor.execute(SELECT_VIEWS, (id,))
            result = self.cursor.fetchone()
            if result is None or len(result) == 0:
                cherrypy.response.status = 400
                return json.dumps({
                    'error': 'video id not found'
                })
            views = result[0] + 1
            self.cursor.execute(INCREMENT_VIEWS, (views, id))
            self.conn.commit()
            self.conn.close()
        except sqlite3.Error as e:
            traceback.print_exc(e)
            cherrypy.response.status = 400
            return json.dumps({
                "error": "failed to query the database"
            })
            self.conn.close()

        return json.dumps({
            'status': 'successfully incremented video views'
        })

    @cherrypy.expose
    def like(self, id=None):
        if not id:
            cherrypy.response.status = 400
            return json.dumps({
                'error': 'No id has been specified'
            })
        try:
            self.conn = sqlite3.connect(VIDEO_DB)
            self.cursor = self.conn.cursor()
            self.cursor.execute(SELECT_LIKES, (id,))
            result = self.cursor.fetchone()
            if result is None or len(result) == 0:
                cherrypy.response.status = 400
                return json.dumps({
                    'error': 'video id not found'
                })
            views = result[0] + 1
            self.cursor.execute(INCREMENT_LIKES, (views, id))
            self.conn.commit()
            self.conn.close()
        except sqlite3.Error as e:
            traceback.print_exc(e)
            cherrypy.response.status = 400
            return json.dumps({
                "error": "failed to query the database"
            })
            self.conn.close()

        return json.dumps({
            'status': 'successfully liked the video'
        })

    @cherrypy.expose
    def dislike(self, id=None):
        if not id:
            cherrypy.response.status = 400
            return json.dumps({
                'error': 'No id has been specified'
            })
        try:
            self.conn = sqlite3.connect(VIDEO_DB)
            self.cursor = self.conn.cursor()
            self.cursor.execute(SELECT_DISLIKES, (id,))
            result = self.cursor.fetchone()
            if result is None or len(result) == 0:
                cherrypy.response.status = 400
                return json.dumps({
                    'error': 'video id not found'
                })
            views = result[0] + 1
            self.cursor.execute(INCREMENT_DISLIKES, (views, id))
            self.conn.commit()
            self.conn.close()
        except sqlite3.Error as e:
            traceback.print_exc(e)
            cherrypy.response.status = 400
            return json.dumps({
                "error": "failed to query the database"
            })
            self.conn.close()

        return json.dumps({
            'status': 'successfully disliked the video'
        })

if __name__ == "__main__":
    cherrypy.config.update({
        'server.socket_host': '0.0.0.0',
        'server.socket_port': 8099, 
        'response.stream': True
        })
    cherrypy.quickstart(VideoServer())
